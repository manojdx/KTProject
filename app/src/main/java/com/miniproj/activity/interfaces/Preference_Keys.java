package com.miniproj.activity.interfaces;

public interface Preference_Keys {

    interface CommonKeys
    {
        String EMPTY = " ";
        Boolean defStatus = true;
        String ContinentID ="continentID";
        String CountryID ="CountryID";
        String ContinentArray ="continentArray";
        String CountryArray ="countryArray";
        Integer Co_pilot = 70;
    }

    interface Faq_Keys
    {
        String mFaq_Response="faqresponse";
    }

    interface Shop_hours_Keys
    {
        String ShopHours = "ShopHours";
    }

    interface LOginKeys {

        String EmailID="emailID";
        String displayName="Name";
        String SID="SID";
        String LoginStatus="LoginStatus";
    }
}
