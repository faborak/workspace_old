package com.yaps.petstore.common.delegate;

import com.yaps.petstore.AbstractTestCase;
import com.yaps.petstore.common.dto.CategoryDTO;
import com.yaps.petstore.common.dto.ItemDTO;
import com.yaps.petstore.common.dto.ProductDTO;
import com.yaps.petstore.common.exception.*;
import com.yaps.petstore.common.rmi.RMIConstant;
import com.yaps.petstore.server.service.catalog.CatalogService;
import junit.framework.TestSuite;
import java.rmi.Naming;
import java.rmi.RemoteException;

/**
 * This class tests the CatalogService class
 */
public final class CatalogDelegateTest extends AbstractTestCase {

    public CatalogDelegateTest(final String s) {
        super(s);
    }

    public static TestSuite suite() {
        return new TestSuite(CatalogDelegateTest.class);
    }

    //==================================
    //=   Test cases for Category      =
    //==================================
    /**
     * This test tries to find an object with a invalid identifier.
     */
    public void testDelegateFindCategoryWithInvalidValues() throws Exception {

        // Binds the RMI service
        bindService();

        // Finds an object with a unknown identifier
        final String id = getPossibleUniqueStringId();
        try {
            new CatalogDelegateFactory().createCatalogDelegate().findCategory(id);
            fail("Object with unknonw id should not be found");
        } catch (ObjectNotFoundException e) {
        }

        // Finds an object with an empty identifier
        try {
            new CatalogDelegateFactory().createCatalogDelegate().findCategory(new String());
            fail("Object with empty id should not be found");
        } catch (ObjectNotFoundException e) {
        }

        // Finds an object with a null identifier
        try {
            new CatalogDelegateFactory().createCatalogDelegate().findCategory(null);
            fail("Object with null id should not be found");
        } catch (ObjectNotFoundException e) {
        }
    }

    /**
     * This test ensures that the method findAll works. It does a first findAll, creates
     * a new object and does a second findAll.
     */
    public void testDelegateFindAllCategories() throws Exception {
        final String id = getPossibleUniqueStringId();

        // First findAll
        final int firstSize = findAllCategories();

        // Creates an object
        createCategory(id);

        // Ensures that the object exists
        try {
            findCategory(id);
        } catch (ObjectNotFoundException e) {
            fail("Object has been created it should be found");
        }

        // Second findAll
        final int secondSize = findAllCategories();

        // Checks that the collection size has increase of one
        if (firstSize + 1 != secondSize) fail("The collection size should have increased by 1");

        // Cleans the test environment
        deleteCategory(id);

        try {
            findCategory(id);
            fail("Object has been deleted it shouldn't be found");
        } catch (ObjectNotFoundException e) {
        }
    }

    /**
     * This method ensures that creating an object works. It first finds the object,
     * makes sure it doesn't exist, creates it and checks it then exists.
     */
    public void testDelegateCreateCategory() throws Exception {
        final String id = getPossibleUniqueStringId();
        CategoryDTO categoryDTO = null;

        // Ensures that the object doesn't exist
        try {
            findCategory(id);
            fail("Object has not been created yet it shouldn't be found");
        } catch (ObjectNotFoundException e) {
        }

        // Creates an object
        createCategory(id);

        // Ensures that the object exists
        try {
            categoryDTO = findCategory(id);
        } catch (ObjectNotFoundException e) {
            fail("Object has been created it should be found");
        }

        // Checks that it's the right object
        checkCategory(categoryDTO, id);

        // Creates an object with the same identifier. An exception has to be thrown
        try {
            createCategory(id);
            fail("An object with the same id has already been created");
        } catch (DuplicateKeyException e) {
        }

        // Cleans the test environment
        deleteCategory(id);

        try {
            findCategory(id);
            fail("Object has been deleted it shouldn't be found");
        } catch (ObjectNotFoundException e) {
        }
    }

    /**
     * This test tries to create an object with a invalid values.
     */
    public void testDelegateCreateCategoryWithInvalidValues() throws Exception {
        CategoryDTO categoryDTO;

        // Binds the RMI service
        bindService();

        // Creates an object with a null parameter
        try {
            new CatalogDelegateFactory().createCatalogDelegate().createCategory(null);
            fail("Object with null parameter should not be created");
        } catch (CreateException e) {
        }

        // Creates an object with empty values
        try {
            categoryDTO = new CategoryDTO(new String(), new String(), new String());
            new CatalogDelegateFactory().createCatalogDelegate().createCategory(categoryDTO);
            fail("Object with empty values should not be created");
        } catch (CheckException e) {
        }

        // Creates an object with null values
        try {
            categoryDTO = new CategoryDTO(null, null, null);
            new CatalogDelegateFactory().createCatalogDelegate().createCategory(categoryDTO);
            fail("Object with null values should not be created");
        } catch (CheckException e) {
        }
    }

    /**
     * This test make sure that updating an object success
     */
    public void testDelegateUpdateCategory() throws Exception {
        final String id = getPossibleUniqueStringId();
        final String updatedId = getPossibleUniqueStringId();

        // Creates an object
        createCategory(id);

        // Ensures that the object exists
        CategoryDTO categoryDTO = null;
        try {
            categoryDTO = findCategory(id);
        } catch (ObjectNotFoundException e) {
            fail("Object has been created it should be found");
        }

        // Checks that it's the right object
        checkCategory(categoryDTO, id);

        // Updates the object with new values
        updateCategory(categoryDTO, updatedId);

        // Ensures that the object still exists
        CategoryDTO categoryUpdated = null;
        try {
            categoryUpdated = findCategory(id);
        } catch (ObjectNotFoundException e) {
            fail("Object should be found");
        }

        // Checks that the object values have been updated
        checkCategory(categoryUpdated, updatedId);

        // Cleans the test environment
        deleteCategory(id);

        try {
            findCategory(id);
            fail("Object has been deleted it shouldn't be found");
        } catch (ObjectNotFoundException e) {
        }
    }

    /**
     * This test tries to update an object with a invalid values.
     */
    public void testDelegateUpdateCategoryWithInvalidValues() throws Exception {
        CategoryDTO categoryDTO;

        // Binds the RMI service
        bindService();

        // Updates an object with a null parameter
        try {
            new CatalogDelegateFactory().createCatalogDelegate().updateCategory(null);
            fail("Object with null parameter should not be updated");
        } catch (UpdateException e) {
        }

        // Updates an object with empty values
        try {
            categoryDTO = new CategoryDTO(new String(), new String(), new String());
            new CatalogDelegateFactory().createCatalogDelegate().updateCategory(categoryDTO);
            fail("Object with empty values should not be updated");
        } catch (CheckException e) {
        }

        // Updates an object with null values
        try {
            categoryDTO = new CategoryDTO(null, null, null);
            new CatalogDelegateFactory().createCatalogDelegate().updateCategory(categoryDTO);
            fail("Object with null values should not be updated");
        } catch (CheckException e) {
        }
    }

    /**
     * This test ensures that the system cannont remove an unknown object
     */
    public void testDelegateDeleteUnknownCategory() throws Exception {
        final String id = getPossibleUniqueStringId();

        // Ensures that the object doesn't exist
        try {
            findCategory(id);
            fail("Object has not been created it shouldn't be found");
        } catch (ObjectNotFoundException e) {
        }

        // Delete the unknown object
        try {
            deleteCategory(id);
            fail("Deleting an unknown object should break");
        } catch (RemoveException e) {
        }
    }


    //==================================
    //=    Test cases for product      =
    //==================================
    /**
     * This test tries to find an object with a invalid identifier.
     */
    public void testDelegateFindProductWithInvalidValues() throws Exception {

        // Binds the RMI service
        bindService();

        // Finds an object with a unknown identifier
        final String id = getPossibleUniqueStringId();
        try {
            new CatalogDelegateFactory().createCatalogDelegate().findProduct(id);
            fail("Object with unknonw id should not be found");
        } catch (ObjectNotFoundException e) {
        }

        // Finds an object with an empty identifier
        try {
            new CatalogDelegateFactory().createCatalogDelegate().findProduct(new String());
            fail("Object with empty id should not be found");
        } catch (CheckException e) {
        }

        // Finds an object with a null identifier
        try {
            new CatalogDelegateFactory().createCatalogDelegate().findProduct(null);
            fail("Object with null id should not be found");
        } catch (CheckException e) {
        }
    }

    /**
     * This test ensures that the method findAll works. It does a first findAll, creates
     * a new object and does a second findAll.
     */
    public void testDelegateFindAllProducts() throws Exception {
        final String id = getPossibleUniqueStringId();

        // First findAll
        final int firstSize = findAllProducts();

        // Creates an object
        createProduct(id);

        // Ensures that the object exists
        try {
            findProduct(id);
        } catch (ObjectNotFoundException e) {
            fail("Object has been created it should be found");
        }

        // Second findAll
        final int secondSize = findAllProducts();

        // Checks that the collection size has increase of one
        if (firstSize + 1 != secondSize) fail("The collection size should have increased by 1");

        // Cleans the test environment
        deleteProduct(id);

        try {
            findProduct(id);
            fail("Object has been deleted it shouldn't be found");
        } catch (ObjectNotFoundException e) {
        }
    }

    /**
     * This test ensures that the method findAll works. It does a first findAll, creates
     * a new object and does a second findAll.
     */
    public void testDelegateFindAllProductsForACategory() throws Exception {
        final String id = getPossibleUniqueStringId();

        // First findAll
        final int firstSize = findAllProducts("cat" + id);

        // Checks that the collection size has increase of one
        if (firstSize != 0) fail("The collection should be empty");

        // Creates an object
        createProduct(id);

        // Ensures that the object exists
        try {
            findProduct(id);
        } catch (ObjectNotFoundException e) {
            fail("Object has been created it should be found");
        }

        // Second findAll
        final int secondSize = findAllProducts("cat" + id);

        // Checks that the collection size has increase of one
        if (firstSize + 1 != secondSize) fail("The collection size should have increased by 1");

        // Create an new object with a different id
        createProduct(id + 1);

        // Ensures that the new object exists
        try {
            findProduct(id + 1);
        } catch (ObjectNotFoundException e) {
            fail("Object has been created it should be found");
        }

        // third findAll
        final int thirdSize = findAllProducts("cat" + id);

        // Checks that the collection size is the same
        // Because the object created had a different id, it should not be in the collection
        if (thirdSize != secondSize) fail("The collection should have the same size");

        // Cleans the test environment
        deleteProduct(id);

        try {
            findProduct(id);
            fail("Object has been deleted it shouldn't be found");
        } catch (ObjectNotFoundException e) {
        }
    }

   /**
     * This method ensures that creating an object works. It first finds the object,
     * makes sure it doesn't exist, creates it and checks it then exists.
     */
    public void testDelegateCreateProduct() throws Exception {
        final String id = getPossibleUniqueStringId();
        ProductDTO productDTO = null;

        // Ensures that the object doesn't exist
        try {
            findProduct(id);
            fail("Object has not been created yet it shouldn't be found");
        } catch (ObjectNotFoundException e) {
        }

        // Creates an object
        createProduct(id);

        // Ensures that the object exists
        try {
            productDTO = findProduct(id);
        } catch (ObjectNotFoundException e) {
            fail("Object has been created it should be found");
        }

        // Checks that it's the right object
        checkProduct(productDTO, id);

        // Creates an object with the same identifier. An exception has to be thrown
        try {
            createProduct(id);
            fail("An object with the same id has already been created");
        } catch (DuplicateKeyException e) {
        }

        // Cleans the test environment
        deleteProduct(id);

        try {
            findProduct(id);
            fail("Object has been deleted it shouldn't be found");
        } catch (ObjectNotFoundException e) {
        }
    }

    /**
     * This test tries to create an object with a invalid values.
     */
    public void testDelegateCreateProductWithInvalidValues() throws Exception {
        ProductDTO productDTO;

        // Binds the RMI service
        bindService();

        // Creates an object with a null parameter
        try {
            new CatalogDelegateFactory().createCatalogDelegate().createProduct(null);
            fail("Object with null parameter should not be created");
        } catch (CreateException e) {
        }

        // Creates an object with empty values
        try {
            productDTO = new ProductDTO(new String(), new String(), new String());
            new CatalogDelegateFactory().createCatalogDelegate().createProduct(productDTO);
            fail("Object with empty values should not be created");
        } catch (CreateException e) {
        }

        // Creates an object with null values
        try {
            productDTO = new ProductDTO(null, null, null);
            new CatalogDelegateFactory().createCatalogDelegate().createProduct(productDTO);
            fail("Object with null values should not be created");
        } catch (CreateException e) {
        }
    }

    /**
     * This test make sure that updating an object success
     */
    public void testDelegateUpdateProduct() throws Exception {
        final String id = getPossibleUniqueStringId();
        final String updatedId = getPossibleUniqueStringId();

        // Creates an object
        createProduct(id);

        // Ensures that the object exists
        ProductDTO productDTO = null;
        try {
            productDTO = findProduct(id);
        } catch (ObjectNotFoundException e) {
            fail("Object has been created it should be found");
        }

        // Checks that it's the right object
        checkProduct(productDTO, id);

        // Updates the object with new values
        updateProduct(productDTO, updatedId);

        // Ensures that the object still exists
        ProductDTO productUpdated = null;
        try {
            productUpdated = findProduct(id);
        } catch (ObjectNotFoundException e) {
            fail("Object should be found");
        }

        // Checks that the object values have been updated
        checkProduct(productUpdated, updatedId);

        // Cleans the test environment
        deleteProduct(id);

        try {
            findProduct(id);
            fail("Object has been deleted it shouldn't be found");
        } catch (ObjectNotFoundException e) {
        }
    }

    /**
     * This test tries to update an object with a invalid values.
     */
    public void testDelegateUpdateProductWithInvalidValues() throws Exception {
        ProductDTO productDTO;

        // Binds the RMI service
        bindService();

        // Updates an object with a null parameter
        try {
            new CatalogDelegateFactory().createCatalogDelegate().updateProduct(null);
            fail("Object with null parameter should not be updated");
        } catch (UpdateException e) {
        }

        // Updates an object with empty values
        try {
            productDTO = new ProductDTO(new String(), new String(), new String());
            new CatalogDelegateFactory().createCatalogDelegate().updateProduct(productDTO);
            fail("Object with empty values should not be updated");
        } catch (CheckException e) {
        }

        // Updates an object with null values
        try {
            productDTO = new ProductDTO(null, null, null);
            new CatalogDelegateFactory().createCatalogDelegate().updateProduct(productDTO);
            fail("Object with null values should not be updated");
        } catch (CheckException e) {
        }
    }

    /**
     * This test ensures that the system cannont remove an unknown object
     */
    public void testDelegateDeleteUnknownProduct() throws Exception {
        final String id = getPossibleUniqueStringId();

        // Ensures that the object doesn't exist
        try {
            findProduct(id);
            fail("Object has not been created it shouldn't be found");
        } catch (ObjectNotFoundException e) {
        }

        // Delete the unknown object
        try {
            deleteProduct(id);
            fail("Deleting an unknown object should break");
        } catch (RemoveException e) {
        }
    }

    //==================================
    //=      Test cases for item       =
    //==================================
    /**
     * This test tries to find an object with a invalid identifier.
     */
    public void testDelegateFindItemWithInvalidValues() throws Exception {

        // Binds the RMI service
        bindService();

        // Finds an object with a unknown identifier
        final String id = getPossibleUniqueStringId();
        try {
            new CatalogDelegateFactory().createCatalogDelegate().findItem(id);
            fail("Object with unknonw id should not be found");
        } catch (ObjectNotFoundException e) {
        }

        // Finds an object with an empty identifier
        try {
            new CatalogDelegateFactory().createCatalogDelegate().findItem(new String());
            fail("Object with empty id should not be found");
        } catch (ObjectNotFoundException e) {
        }

        // Finds an object with a null identifier
        try {
            new CatalogDelegateFactory().createCatalogDelegate().findItem(null);
            fail("Object with null id should not be found");
        } catch (ObjectNotFoundException e) {
        }
    }

    /**
     * This test ensures that the method findAll works. It does a first findAll, creates
     * a new object and does a second findAll.
     */
    public void testDelegateFindAllItems() throws Exception {
        final String id = getPossibleUniqueStringId();

        // First findAll
        final int firstSize = findAllItems();

        // Creates an object
        createItem(id);

        // Ensures that the object exists
        try {
            findItem(id);
        } catch (ObjectNotFoundException e) {
            fail("Object has been created it should be found");
        }

        // Second findAll
        final int secondSize = findAllItems();

        // Checks that the collection size has increase of one
        if (firstSize + 1 != secondSize) fail("The collection size should have increased by 1");

        // Cleans the test environment
        deleteItem(id);

        try {
            findItem(id);
            fail("Object has been deleted it shouldn't be found");
        } catch (ObjectNotFoundException e) {
        }
    }

    /**
     * This test ensures that the method findAll works. It does a first findAll, creates
     * a new object and does a second findAll.
     */
    public void testDelegateFindAllItemsForAProduct() throws Exception {
        final String id = getPossibleUniqueStringId();

        // First findAll
        final int firstSize = findAllItems("prod" + id);

        // Checks that the collection size has increase of one
        if (firstSize != 0) fail("The collection should be empty");

        // Creates an object
        createItem(id);

        // Ensures that the object exists
        try {
            findItem(id);
        } catch (ObjectNotFoundException e) {
            fail("Object has been created it should be found");
        }

        // Second findAll
        final int secondSize = findAllItems("prod" + id);

        // Checks that the collection size has increase of one
        if (firstSize + 1 != secondSize) fail("The collection size should have increased by 1");

        // Create an new object with a different id
        createItem(id + 1);

        // Ensures that the new object exists
        try {
            findItem(id + 1);
        } catch (ObjectNotFoundException e) {
            fail("Object has been created it should be found");
        }

        // third findAll
        final int thirdSize = findAllItems("prod" + id);

        // Checks that the collection size is the same
        // Because the object created had a different id, it should not be in the collection
        if (thirdSize != secondSize) fail("The collection should have the same size");

        // Cleans the test environment
        deleteItem(id);

        try {
            findItem(id);
            fail("Object has been deleted it shouldn't be found");
        } catch (ObjectNotFoundException e) {
        }
    }

    /**
     * This test ensures that the method search works. It does a first search, creates
     * a new object and does a second search.
     */
    public void testDelegateSearchItems() throws Exception {
        final String id = getPossibleUniqueStringId();

        // First search
        final int firstSize = searchItems(id);

        // Creates an object
        createItem(id);

        // Ensures that the object exists
        try {
            findItem(id);
        } catch (ObjectNotFoundException e) {
            fail("Object has been created it should be found");
        }

        // Second search
        final int secondSize = searchItems(id);

        // Checks that the collection size has increase of one
        if (firstSize + 1 != secondSize) fail("The collection size should have increased by 1");

        // Create an new object with a different id
        createItem(String.valueOf(Integer.parseInt(id) + 1));

        // Ensures that the new object exists
        try {
            findItem(String.valueOf(Integer.parseInt(id) + 1));
        } catch (ObjectNotFoundException e) {
            fail("Object has been created it should be found");
        }

        // third search
        final int thirdSize = searchItems(id);

        // Checks that the collection size is the same
        // Because the object created had a different id, it should not be in the collection
        if (thirdSize != secondSize) fail("The collection should have the same size");

        // Cleans the test environment
        deleteItem(id);

        try {
            findItem(id);
            fail("Object has been deleted it shouldn't be found");
        } catch (ObjectNotFoundException e) {
        }
    }

    /**
     * This method ensures that creating an object works. It first finds the object,
     * makes sure it doesn't exist, creates it and checks it then exists.
     */
    public void testDelegateCreateItem() throws Exception {
        final String id = getPossibleUniqueStringId();
        ItemDTO itemDTO = null;

        // Ensures that the object doesn't exist
        try {
            findItem(id);
            fail("Object has not been created yet it shouldn't be found");
        } catch (ObjectNotFoundException e) {
        }

        // Creates an object
        createItem(id);

        // Ensures that the object exists
        try {
            itemDTO = findItem(id);
        } catch (ObjectNotFoundException e) {
            fail("Object has been created it should be found");
        }

        // Checks that it's the right object
        checkItem(itemDTO, id);

        // Cleans the test environment
        deleteItem(id);

        try {
            findItem(id);
            fail("Object has been deleted it shouldn't be found");
        } catch (ObjectNotFoundException e) {
        }
    }

    /**
     * This test tries to create an object with a invalid values.
     */
    public void testDelegateCreateItemWithInvalidValues() throws Exception {
        ItemDTO itemDTO;

        // Binds the RMI service
        bindService();

        // Creates an object with a null parameter
        try {
            new CatalogDelegateFactory().createCatalogDelegate().createItem(null);
            fail("Object with null parameter should not be created");
        } catch (CreateException e) {
        }

        // Creates an object with empty values
        try {
            itemDTO = new ItemDTO(new String(), new String(), 0);
            new CatalogDelegateFactory().createCatalogDelegate().createItem(itemDTO);
            fail("Object with empty values should not be created");
        } catch (CheckException e) {
        }

        // Creates an object with null values
        try {
            itemDTO = new ItemDTO(null, null, 0);
            new CatalogDelegateFactory().createCatalogDelegate().createItem(itemDTO);
            fail("Object with null values should not be created");
        } catch (CheckException e) {
        }
    }

    /**
     * This test make sure that updating an object success
     */
    public void testDelegateUpdateItem() throws Exception {
        final String id = getPossibleUniqueStringId();
        final String updatedId = getPossibleUniqueStringId();

        // Creates an object
        createItem(id);

        // Ensures that the object exists
        ItemDTO itemDTO = null;
        try {
            itemDTO = findItem(id);
        } catch (ObjectNotFoundException e) {
            fail("Object has been created it should be found");
        }

        // Checks that it's the right object
        checkItem(itemDTO, id);

        // Updates the object with new values
        updateItem(itemDTO, updatedId);

        // Ensures that the object still exists
        ItemDTO itemUpdated = null;
        try {
            itemUpdated = findItem(id);
        } catch (ObjectNotFoundException e) {
            fail("Object should be found");
        }

        // Checks that the object values have been updated
        checkItem(itemUpdated, updatedId);

        // Cleans the test environment
        deleteItem(id);

        try {
            findItem(id);
            fail("Object has been deleted it shouldn't be found");
        } catch (ObjectNotFoundException e) {
        }
    }

    /**
     * This test tries to update an object with a invalid values.
     */
    public void testDelegateUpdateItemWithInvalidValues() throws Exception {
        ItemDTO itemDTO;

        // Binds the RMI service
        bindService();

        // Updates an object with a null parameter
        try {
            new CatalogDelegateFactory().createCatalogDelegate().updateItem(null);
            fail("Object with null parameter should not be updated");
        } catch (UpdateException e) {
        }

        // Updates an object with empty values
        try {
            itemDTO = new ItemDTO(new String(), new String(), 0);
            new CatalogDelegateFactory().createCatalogDelegate().updateItem(itemDTO);
            fail("Object with empty values should not be updated");
        } catch (CheckException e) {
        }

        // Updates an object with null values
        try {
            itemDTO = new ItemDTO(null, null, 0);
            new CatalogDelegateFactory().createCatalogDelegate().updateItem(itemDTO);
            fail("Object with null values should not be updated");
        } catch (CheckException e) {
        }
    }

    /**
     * This test ensures that the system cannont remove an unknown object
     */
    public void testDelegateDeleteUnknownItem() throws Exception {
        final String id = getPossibleUniqueStringId();

        // Ensures that the object doesn't exist
        try {
            findItem(id);
            fail("Object has not been created it shouldn't be found");
        } catch (ObjectNotFoundException e) {
        }

        // Delete the unknown object
        try {
            deleteItem(id);
            fail("Deleting an unknown object should break");
        } catch (RemoveException e) {
        }
    }

    //==================================
    //=  Private Methods for Category  =
    //==================================
    private CategoryDTO findCategory(final String id) throws FinderException, CheckException {
        CategoryDTO categoryDTO = null;

        // Binds the RMI service
        bindService();
        try {
            categoryDTO = new CatalogDelegateFactory().createCatalogDelegate().findCategory("cat" + id);
        } catch (RemoteException e) {
            fail("Service is bound. Call should have succed");
        }

        // Unbinds the RMI service
        if ( !unbindService() ) return categoryDTO;
        try {
            categoryDTO = new CatalogDelegateFactory().createCatalogDelegate().findCategory("cat" + id);
            fail("Service is not bound. Call should have failed");
        } catch (RemoteException e) {
        }

        // Binds the RMI service
        bindService();

        return categoryDTO;
    }

    private int findAllCategories() throws FinderException, RemoteException {
        // Binds the RMI service
        bindService();

        try {
            return new CatalogDelegateFactory().createCatalogDelegate().findCategories().size();
        } catch (ObjectNotFoundException e) {
            return 0;
        }
    }

    private void createCategory(final String id) throws CreateException, CheckException {
        // Binds the RMI service
        bindService();

        final CategoryDTO categoryDTO = new CategoryDTO("cat" + id, "name" + id, "description" + id);

        try {
            new CatalogDelegateFactory().createCatalogDelegate().createCategory(categoryDTO);
        } catch (RemoteException e) {
            fail("Service is bound. Call should have succed");
        }

        // Unbinds the RMI service
        if ( !unbindService() ) return;

        try {
            new CatalogDelegateFactory().createCatalogDelegate().createCategory(categoryDTO);
            fail("Service is not bound. Call should have failed");
        } catch (RemoteException e) {
        }

        // Binds the RMI service
        bindService();
    }

    private void updateCategory(final CategoryDTO categoryDTO, final String id) throws UpdateException, CheckException, RemoteException {
        // Binds the RMI service
        bindService();

        categoryDTO.setName("name" + id);
        categoryDTO.setDescription("description" + id);

        new CatalogDelegateFactory().createCatalogDelegate().updateCategory(categoryDTO);
    }

    private void deleteCategory(final String id) throws RemoveException, CheckException, RemoteException {
        // Binds the RMI service
        bindService();

        new CatalogDelegateFactory().createCatalogDelegate().deleteCategory("cat" + id);
    }

    private void checkCategory(final CategoryDTO categoryDTO, final String id) {
        assertEquals("name", "name" + id, categoryDTO.getName());
        assertEquals("description", "description" + id, categoryDTO.getDescription());
    }

    //==================================
    //=  Private Methods for Product   =
    //==================================
    private ProductDTO findProduct(final String id) throws FinderException, CheckException {
        ProductDTO productDTO = null;

        // Binds the RMI service
        bindService();

        try {
            productDTO = new CatalogDelegateFactory().createCatalogDelegate().findProduct("prod" + id);
        } catch (RemoteException e) {
            fail("Service is bound. Call should have succed");
        }

        // Unbinds the RMI service
        if ( !unbindService() ) return productDTO;

        try {
            productDTO = new CatalogDelegateFactory().createCatalogDelegate().findProduct("prod" + id);
            fail("Service is not bound. Call should have failed");
        } catch (RemoteException e) {
        }

        // Binds the RMI service
        bindService();

        return productDTO;
    }

    private int findAllProducts() throws FinderException, RemoteException {
        // Binds the RMI service
        bindService();

        try {
            return new CatalogDelegateFactory().createCatalogDelegate().findProducts().size();
        } catch (ObjectNotFoundException e) {
            return 0;
        }
    }

    private int findAllProducts(String categoryId) throws FinderException, RemoteException {
        // Binds the RMI service
        bindService();

        try {
            return new CatalogDelegateFactory().createCatalogDelegate().findProducts(categoryId).size();
        } catch (ObjectNotFoundException e) {
            return 0;
        }
    }

    // Creates a category first and then a product linked to this category
    private void createProduct(final String id) throws CreateException, CheckException {
        // Binds the RMI service
        bindService();

        final CategoryDTO categoryDTO = new CategoryDTO("cat" + id, "name" + id, "description" + id);
        final ProductDTO productDTO = new ProductDTO("prod" + id, "name" + id, "description" + id);
        productDTO.setCategoryId("cat" + id);

        try {
            new CatalogDelegateFactory().createCatalogDelegate().createCategory(categoryDTO);
            new CatalogDelegateFactory().createCatalogDelegate().createProduct(productDTO);
        } catch (RemoteException e) {
            fail("Service is bound. Call should have succed");
        }

        // Unbinds the RMI service
        if ( !unbindService() ) return;

        try {
            new CatalogDelegateFactory().createCatalogDelegate().createCategory(categoryDTO);
            new CatalogDelegateFactory().createCatalogDelegate().createProduct(productDTO);
            fail("Service is not bound. Call should have failed");
        } catch (RemoteException e) {
        }

        // Binds the RMI service
        bindService();
    }

    // Creates a category and updates the product with this new category
    private void updateProduct(final ProductDTO productDTO, final String id) throws UpdateException, CheckException, RemoteException, CreateException {
        // Binds the RMI service
        bindService();

        // Create Category
        final CategoryDTO categoryDTO = new CategoryDTO("cat" + id, "name" + id, "description" + id);
        new CatalogDelegateFactory().createCatalogDelegate().createCategory(categoryDTO);
        // Update Product with new category
        productDTO.setName("name" + id);
        productDTO.setDescription("description" + id);
        productDTO.setCategoryId("cat" + id);
        new CatalogDelegateFactory().createCatalogDelegate().updateProduct(productDTO);
    }

    private void deleteProduct(final String id) throws RemoveException, CheckException, RemoteException {
        // Binds the RMI service
        bindService();

        new CatalogDelegateFactory().createCatalogDelegate().deleteProduct("prod" + id);
        new CatalogDelegateFactory().createCatalogDelegate().deleteCategory("cat" + id);
    }

    private void checkProduct(final ProductDTO productDTO, final String id) {
        assertEquals("name", "name" + id, productDTO.getName());
        assertEquals("description", "description" + id, productDTO.getDescription());
    }

    //==================================
    //=    Private Methods for Item    =
    //==================================
    private ItemDTO findItem(final String id) throws FinderException, CheckException {
        ItemDTO itemDTO = null;

        // Binds the RMI service
        bindService();

        try {
            itemDTO = new CatalogDelegateFactory().createCatalogDelegate().findItem("item" + id);
        } catch (RemoteException e) {
            fail("Service is bound. Call should have succed");
        }

        // Unbinds the RMI service
        if ( !unbindService() ) return itemDTO;

        try {
            itemDTO = new CatalogDelegateFactory().createCatalogDelegate().findItem("item" + id);
            fail("Service is not bound. Call should have failed");
        } catch (RemoteException e) {
        }

        // Binds the RMI service
        bindService();

        return itemDTO;
    }

    private int findAllItems() throws FinderException, RemoteException {
        // Binds the RMI service
        bindService();

        try {
            return new CatalogDelegateFactory().createCatalogDelegate().findItems().size();
        } catch (ObjectNotFoundException e) {
            return 0;
        }
    }

    private int findAllItems(String productId) throws FinderException, RemoteException {
        // Binds the RMI service
        bindService();

        try {
            return new CatalogDelegateFactory().createCatalogDelegate().findItems(productId).size();
        } catch (ObjectNotFoundException e) {
            return 0;
        }
    }

    private int searchItems(String keyword) throws FinderException, RemoteException {
        // Binds the RMI service
        bindService();

        try {
            return new CatalogDelegateFactory().createCatalogDelegate().searchItems(keyword).size();
        } catch (ObjectNotFoundException e) {
            return 0;
        }
    }
    
    // Creates a category first, then a product and then an item linked to this product
    private void createItem(final String id) throws CreateException, CheckException {
        // Binds the RMI service
        bindService();

        final CategoryDTO categoryDTO = new CategoryDTO("cat" + id, "name" + id, "description" + id);
        final ProductDTO productDTO = new ProductDTO("prod" + id, "name" + id, "description" + id);
        productDTO.setCategoryId("cat" + id);
        final ItemDTO itemDTO = new ItemDTO("item" + id, "name" + id, Double.parseDouble(id));
        itemDTO.setImagePath("imagePath" + id);
        itemDTO.setProductId("prod" + id);

        try {
            new CatalogDelegateFactory().createCatalogDelegate().createCategory(categoryDTO);
            new CatalogDelegateFactory().createCatalogDelegate().createProduct(productDTO);
            new CatalogDelegateFactory().createCatalogDelegate().createItem(itemDTO);
        } catch (RemoteException e) {
            fail("Service is bound. Call should have succed");
        }

        // Unbinds the RMI service
        if ( !unbindService() ) return;

        try {
            new CatalogDelegateFactory().createCatalogDelegate().createCategory(categoryDTO);
            new CatalogDelegateFactory().createCatalogDelegate().createProduct(productDTO);
            new CatalogDelegateFactory().createCatalogDelegate().createItem(itemDTO);
            fail("Service is not bound. Call should have failed");
        } catch (RemoteException e) {
        }

        // Binds the RMI service
        bindService();
    }

    // Creates a category, a product and updates the item with this new product
    private void updateItem(final ItemDTO itemDTO, final String id) throws UpdateException, CheckException, RemoteException, CreateException {
        // Binds the RMI service
        bindService();

        // Create Category
        final CategoryDTO categoryDTO = new CategoryDTO("cat" + id, "name" + id, "description" + id);
        new CatalogDelegateFactory().createCatalogDelegate().createCategory(categoryDTO);
        // Create Product
        final ProductDTO productDTO = new ProductDTO("prod" + id, "name" + id, "description" + id);
        productDTO.setCategoryId("cat" + id);
        new CatalogDelegateFactory().createCatalogDelegate().createProduct(productDTO);
        // Updates the item
        itemDTO.setName("name" + id);
        itemDTO.setUnitCost(Double.parseDouble(id));
        itemDTO.setImagePath("imagePath" + id);
        itemDTO.setProductId("prod" + id);
        new CatalogDelegateFactory().createCatalogDelegate().updateItem(itemDTO);
    }

    private void deleteItem(final String id) throws RemoveException, CheckException, RemoteException {
        // Binds the RMI service
        bindService();

        new CatalogDelegateFactory().createCatalogDelegate().deleteItem("item" + id);
        new CatalogDelegateFactory().createCatalogDelegate().deleteProduct("prod" + id);
        new CatalogDelegateFactory().createCatalogDelegate().deleteCategory("cat" + id);
    }

    private void checkItem(final ItemDTO itemDTO, final String id) {
        assertEquals("name", "name" + id, itemDTO.getName());
        assertEquals("unitCost", new Double(id), new Double(itemDTO.getUnitCost()));
        assertEquals("imagePath", "imagePath" + id, itemDTO.getImagePath());
    }

    //==================================
    //=         Private Methods        =
    //==================================
    private boolean bindService() {
    	CatalogDelegate CatalogDelegate = new CatalogDelegateFactory().createCatalogDelegate();
    	if (CatalogDelegate instanceof  CatalogLocalDelegate )
			return true;
        try {
            Naming.rebind(RMIConstant.CATALOG_SERVICE, new CatalogService());
        } catch (Exception e) {
            fail("Could not bind a service");
            return false;
        }
        return true;
    }

    private boolean unbindService() {
    	CatalogDelegate CatalogDelegate = new CatalogDelegateFactory().createCatalogDelegate();
    	if (CatalogDelegate instanceof  CatalogLocalDelegate )
			return false;
        try {
            Naming.unbind(RMIConstant.CATALOG_SERVICE);
        } catch (Exception e) {
            fail("Could not unbind a service");
            return false;
        }
        return true;
    }
}