/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package br.com.sharkweb.fbv;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;

import br.com.sharkweb.fbv.Util.Constantes;
import br.com.sharkweb.fbv.model.TimeP;
import br.com.sharkweb.fbv.model.TimeUsuarioP;


public class StarterApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        // Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Constantes.setParseApplicationId("wmmuV3lyE7UQlyECrJwjJB22D03RD0gWZ3Ate89K");
        Constantes.setParseClientKey("7IegdKGnREcqge6xHTWW3YnRiRt7CJAiZOmpZTDm");

      //  ParseObject.registerSubclass(TimeUsuarioP.class);
       // ParseObject.registerSubclass(TimeP.class);
        Parse.initialize(this, Constantes.getParseApplicationId(), Constantes.getParseClientKey());
        ParseInstallation.getCurrentInstallation().saveEventually();
        // ParseUser.enableAutomaticUser();
        //ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        // defaultACL.setPublicReadAccess(true);
        //ParseACL.setDefaultACL(defaultACL, true);
    }
}
