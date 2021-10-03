package com.tecmanic.goservices.Extra;

import java.util.ArrayList;

import com.tecmanic.goservices.ModelClass.SavedAddress;

/**
 * Created by Nabeel on 3/13/2018.
 */

public interface SavedPlaceListener {
    public void onSavedPlaceClick(ArrayList<SavedAddress> mResultList, int position);
}
