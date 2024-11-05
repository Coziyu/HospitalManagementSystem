package org.hms.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class PersonalParticulars implements Serializable {
    String name;
    Date dateOfBirth;
    String gender;
    ArrayList<ContactInformation> contactRecords;
}
