package com.yaps.petstore.server.service.customer;

import com.yaps.petstore.common.dto.CustomerDTO;
import com.yaps.petstore.common.exception.*;

import java.util.Collection;
import javax.ejb.Remote;

/**
 * This interface gives a remote view of the CustomerServiceBean. Any distant client that wants to call
 * a method on the CustomerServiceBean has to use this interface.
 */
@Remote
public interface CustomerService {

    // ======================================
    // =           Business methods         =
    // ======================================

    /**
     * Given an id and password, this method checks if the password is the right one for
     * this customer identifier. If it matches, the method returns a CustomerDTO with
     * all the customer data.
     *
     * @param customerId
     * @param password   to be checked if it's the right one
     * @return CustomerDTO customer data
     * @throws FinderException is thrown if a DomainException is caught
     *                         or a system failure is occurs
     * @throws CheckException  is thrown if a invalid data or password is found
     */
    CustomerDTO authenticate(String customerId, String password) throws FinderException, CheckException;

    /**
     * Given a CustomerDTO object, this method creates a Customer. It first transforms
     * a CustomerDTO into a Customer domain object, uses the Customer object to apply the
     * business rules for creation. It then transforms back the Customer object into a
     * CustomerDTO.
     *
     * @param customerDTO cannot be null.
     * @return the created CustomerDTO
     * @throws DuplicateKeyException is thrown if an object with the same id
     *                               already exists in the system
     * @throws CreateException       is thrown if a DomainException is caught
     *                               or a system failure is occurs
     * @throws CheckException        is thrown if a invalid data is found
     */
    CustomerDTO createCustomer(CustomerDTO customerDTO) throws CreateException, CheckException;

    /**
     * Given an id this method uses the Customer domain object to load all the data of this
     * object. It then transforms this object into a CustomerDTO and returns it.
     *
     * @param customerId identifier
     * @return CustomerDTO
     * @throws ObjectNotFoundException is thrown if no object with this given id is found
     * @throws FinderException         is thrown if a DomainException is caught
     *                                 or a system failure is occurs
     * @throws CheckException          is thrown if a invalid data is found
     */
    CustomerDTO findCustomer(String customerId) throws FinderException, CheckException;

    /**
     * Given an id, this method finds a Customer domain object and then calls its deletion
     * method.
     *
     * @param customerId identifier
     * @throws RemoveException is thrown if a DomainException is caught
     *                         or a system failure is occurs
     * @throws CheckException  is thrown if a invalid data is found
     */
    void deleteCustomer(String customerId) throws RemoveException, CheckException;

    /**
     * Given a CustomerDTO object, this method updates a Customer. It first transforms
     * a CustomerDTO into a Customer domain object and uses the Customer object to apply the
     * business rules for modification.
     *
     * @param customerDTO cannot be null.
     * @throws UpdateException is thrown if a DomainException is caught
     *                         or a system failure is occurs
     * @throws CheckException  is thrown if a invalid data is found
     */
    void updateCustomer(CustomerDTO customerDTO) throws UpdateException, CheckException;

    /**
     * This method return all the customers from the system. It uses the Customer domain object
     * to get the data. It then transforms this collection of Customer object into a
     * collection of CustomerDTO and returns it.
     *
     * @return a collection of CustomerDTO
     * @throws ObjectNotFoundException is thrown if the collection is empty
     */
    Collection findCustomers() throws FinderException;
}