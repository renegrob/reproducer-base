'use strict';

// https://auth.automatix:8443/

function checkCapabilities() {
    if (window.PublicKeyCredential) {
       // code here
       console.log("use public-key credentials");
    }else{
       alert("public-key credentials not supported");
    }

    PublicKeyCredential.isUserVerifyingPlatformAuthenticatorAvailable().then(
       function() {
           console.log("User verifying platform available");
       }
    );
}

function register() {
    // checkCapabilities();

    // https://www.w3.org/TR/webauthn-2/#dictdef-publickeycredentialcreationoptions
    var publicKeyOptions = {

      // Relying Party:
      rp: {
        name: "The Secure Page",
        id: "auth.automatix"
      },

      // User:
      user: {
        //id: Uint8Array.from(window.atob("MIIBkzCCATigAwIBAjCCAZMwggE4oAMCAQIwggGTMII="), c=>c.charCodeAt(0)),
        id : new Uint8Array([11,31,105,21,31,103,21,31,105,21,1,2,4,5,6,7]),
//        name: "alex.mueller@example.com",
//        displayName: "Alex Müller",
        name: "johndoe",
        displayName: "Jonny",
      },

      // The challenge is produced by the server; see the Security Considerations (this challenge is very short!)
      challenge: new Uint8Array([21,31,105,21,31,105,21,31,105,21,31,105,21,31,105,99,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0]),


      // This Relying Party will accept either an ES256 or RS256 credential, but
      // prefers an ES256 credential.
      pubKeyCredParams: [
        {
          type: "public-key",
          alg: -7 // "ES256" as registered in the IANA COSE Algorithms registry
        },
        {type: "public-key", alg: -35},
        {type: "public-key", alg: -36},
        {
          type: "public-key",
          alg: -257 // Value registered by this specification for "RS256"
        },
        {type: "public-key", alg: -257},
        {type: "public-key", alg: -258},
        {type: "public-key", alg: -259},
        {type: "public-key", alg: -37},
        {type: "public-key", alg: -38},
        {type: "public-key", alg: -39},
        {type: "public-key", alg: -8}
      ],
/*
      authenticatorSelection: {
        // Try to use UV if possible. This is also the default. (e.g. Key needs to be touched)
        userVerification: "required",
        // client-side discoverable credential (vs server-side generated key)
        residentKey: "required",
        requireResidentKey: true
      },

      // authenticatorSelection: {authenticatorAttachment: "cross-platform", requireResidentKey: true, userVerification: "required"}

      timeout: 360000,  // 6 minutes

      // helps to avoid double registration
      /*
      excludeCredentials: [
        // Don’t re-register any authenticator that has one of these credentials
        {"id": Uint8Array.from(window.atob("ufJWp8YGlibm1Kd9XQBWN1WAw2jy5In2Xhon9HAqcXE="), c=>c.charCodeAt(0)), "type": "public-key"},
        {"id": Uint8Array.from(window.atob("E/e1dhZc++mIsz4f9hb6NifAzJpF1V4mEtRlIPBiWdY="), c=>c.charCodeAt(0)), "type": "public-key"}
      ],
      */
      //attestation: "direct",  // requires user's consent
      attestation: "none"

      // Make excludeCredentials check backwards compatible with credentials registered with U2F
      //extensions: {"appidExclude": "https://acme.example.com"}
      // extensions: {txAuthSimple: ""}
    };

    console.log("PublicKeyCredentialCreationOptions: " + JSON.stringify(publicKeyOptions))

    // Note: The following call will cause the authenticator to display UI.
    navigator.credentials.create({ "publicKey": publicKeyOptions })
        .then(
      function (newCredentialInfo) {
        // Send new credential info to server for verification and registration.
        console.log("new credentials: ")
        console.log(newCredentialInfo);
        console.log(newCredentialInfo.id);
        console.log(newCredentialInfo.rawId);
      }
      ).catch(function (err) {
        // No acceptable authenticator or user refused consent. Handle appropriately.
        alert(err);
      });

}

function login() {

    // checkCapabilities();

    const base64Id = "KmpcXOUmS5ddq2nFBF25NSV5RIqheOK7gJmmhBKLq4m_4caGLaaRzbwvE65Pv3Y6r8_p5UPr_7OzJMVlg2ekaHCGPYufGNPT5Z66UJKI4ir4ag7BpTHqyCkVWZ2DfLix";
    var challengeBuffer = new Uint8Array([21,31,105,21,31,105,21,31,105,21,31,105,21,31,105,99,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,50]);
    //var idBuffer = Uint8Array.from(atob(base64Id), c => c.charCodeAt(0));
    // var idBuffer = Base64Binary.decodeArrayBuffer(base64Id);
    var idBuffer = new Uint8Array([42, 106, 92, 92, -27, 38, 75, -105, 93, -85, 105, -59, 4, 93, -71, 53, 37, 121, 68, -118, -95, 120, -30, -69, -128, -103, -90, -124, 18, -117, -85, -119, -65, -31, -58, -122, 45, -90, -111, -51, -68, 47, 19, -82, 79, -65, 118, 58, -81, -49, -23, -27, 67, -21, -1, -77, -77, 36, -59, 101, -125, 103, -92, 104, 112, -122, 61, -117, -97, 24, -45, -45, -27, -98, -70, 80, -110, -120, -30, 42, -8, 106, 14, -63, -91, 49, -22, -56, 41, 21, 89, -99, -125, 124, -72, -79]);

    // navigator.credentials.preventSilentAccess();

    const publicKeyCredentialRequestOptions = {
        challenge: challengeBuffer,
        allowCredentials: [{
            type: 'public-key',
            id : idBuffer,
            transports: [   "usb",
                                "nfc",
                                "ble",
                                "internal"],
        }],
        userVerification: "preferred",  // use "required"
        /*
      authenticatorSelection: {
        // Try to use UV if possible. This is also the default.
        userVerification: "required"
      },*/
      //  timeout: 60000,
    }
    /*
    const assertion = await navigator.credentials.get({
        publicKey: publicKeyCredentialRequestOptions
    });
    */

    console.log("publicKeyCredentialRequestOptions: ")
    console.log(publicKeyCredentialRequestOptions);

    navigator.credentials.get({ "publicKey": publicKeyCredentialRequestOptions })
      .then(
      function (credential) {
        // Send new credential info to server for verification and registration.
        console.log("credential: ")
        console.log(credential);
      }
      );

/*
    navigator.credentials.get({
        publicKey: {
            challenge: challengeBuffer,
            userVerification: "required"
        },
    })
    .then(
      function (credential) {
        // Send new credential info to server for verification and registration.
        console.log("credential: ")
        console.log(credential);
      }
    );
    */
}

/*
publicKey:
attestation: "direct"
authenticatorSelection: {authenticatorAttachment: "cross-platform", requireResidentKey: true, userVerification: "required"}
challenge: Uint8Array(32) [206, 28, 246, 204, 100, 152, 219, 25, 153, 104, 41, 106, 209, 120, 227, 89, 158, 147, 37, 44, 38, 11, 135, 151, 245, 20, 238, 10, 173, 3, 73, 102]
pubKeyCredParams: (10) [{…}, {…}, {…}, {…}, {…}, {…}, {…}, {…}, {…}, {…}]
0: {type: "public-key", alg: -7}
1: {type: "public-key", alg: -35}
2: {type: "public-key", alg: -36}
3: {type: "public-key", alg: -257}
4: {type: "public-key", alg: -258}
5: {type: "public-key", alg: -259}
6: {type: "public-key", alg: -37}
7: {type: "public-key", alg: -38}
8: {type: "public-key", alg: -39}
9: {type: "public-key", alg: -8}
rp: {name: "webauthn.io", id: "webauthn.io"}
timeout: 60000
user: {name: "john-doe", displayName: "john-doe", id: Uint8Array(10)}
__proto__: Object
__proto__: Object
*/