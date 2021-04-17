package me.larux.lsupport.storage.factory;

import me.larux.lsupport.storage.partner.Partner;
import me.raider.plib.commons.serializer.SerializedObject;
import me.raider.plib.commons.serializer.factory.InstanceFactory;

import java.util.Map;

public class PartnerFactory implements InstanceFactory<Partner> {
    @Override
    public Partner create(SerializedObject serializedObject) {
        Map<String, Object> serializedMap = serializedObject.getLinkedMap();

        if (serializedMap.get("id")==null) {
            return null;
        }

        Partner partner = new Partner((String) serializedMap.get("id"));

        if (serializedMap.get("partners")!=null) {
            partner.setPartners((Integer) serializedMap.get("partners"));
        }

        return partner;
    }
}
