package org.hms.services.drugdispensary;

import java.io.Serializable;

public class DrugRequest implements Serializable {
    String drugName;
    DrugRequestStatus status;
}
