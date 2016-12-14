package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.headerlesscontainer.gobii.ContactDTO;

import java.util.List;


/**
 * Created by Angel on 5/4/2016.
 */
public interface ContactService {

    List<ContactDTO> getContacts() throws GobiiDomainException;
    ContactDTO createContact(ContactDTO contactDTO) throws GobiiDomainException;
    ContactDTO replaceContact(Integer contactId, ContactDTO contactDTO) throws GobiiDomainException;
    ContactDTO getContactById(Integer contactId) throws GobiiDomainException;
    ContactDTO getContactByEmail(String email) throws GobiiDomainException;
    ContactDTO getContactByLastName(String lastName) throws GobiiDomainException;
    ContactDTO getContactByFirstName(String email, String lastName, String firstName) throws GobiiDomainException;


}
