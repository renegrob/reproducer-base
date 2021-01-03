'use strict';

// https://auth.automatix:8443/


// -------------

  /*
   * Base64URL-ArrayBuffer
   * https://github.com/herrjemand/Base64URL-ArrayBuffer
   *
   * Copyright (c) 2017 Yuriy Ackermann <ackermann.yuriy@gmail.com>
   * Copyright (c) 2012 Niklas von Hertzen
   * Licensed under the MIT license.
   */

  const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_';
  // Use a lookup table to find the index.
  const lookup = new Uint8Array(256);

  for (var i = 0; i < chars.length; i++) {
    lookup[chars.charCodeAt(i)] = i;
  }

  const bufferToBase64 = function (arraybuffer) {
    const bytes = new Uint8Array(arraybuffer);

    var len = bytes.length;
    var base64url = '';

    for (var i = 0; i < len; i += 3) {
      base64url += chars[bytes[i] >> 2];
      base64url += chars[((bytes[i] & 3) << 4) | (bytes[i + 1] >> 4)];
      base64url += chars[((bytes[i + 1] & 15) << 2) | (bytes[i + 2] >> 6)];
      base64url += chars[bytes[i + 2] & 63];
    }

    if ((len % 3) === 2) {
      base64url = base64url.substring(0, base64url.length - 1);
    } else if (len % 3 === 1) {
      base64url = base64url.substring(0, base64url.length - 2);
    }

    return base64url;
  }

  const base64ToBuffer = function (base64string) {
    if (base64string) {

      var bufferLength = base64string.length * 0.75;

      var len = base64string.length;
      var p = 0;

      var encoded1;
      var encoded2;
      var encoded3;
      var encoded4;

      let bytes = new Uint8Array(bufferLength);

      for (var i = 0; i < len; i += 4) {
        encoded1 = lookup[base64string.charCodeAt(i)];
        encoded2 = lookup[base64string.charCodeAt(i + 1)];
        encoded3 = lookup[base64string.charCodeAt(i + 2)];
        encoded4 = lookup[base64string.charCodeAt(i + 3)];

        bytes[p++] = (encoded1 << 2) | (encoded2 >> 4);
        bytes[p++] = ((encoded2 & 15) << 4) | (encoded3 >> 2);
        bytes[p++] = ((encoded3 & 3) << 6) | (encoded4 & 63);
      }

      return bytes.buffer;
    }
  }

  // ------------------


function checkCapabilities() {
    if (window.PublicKeyCredential) {
       // code here
       console.log("use public-key credentials");

       PublicKeyCredential.isUserVerifyingPlatformAuthenticatorAvailable().then(
          function() {
              console.log("User verifying platform available");
          }
       );

    } else {
       alert("public-key credentials not supported");
    }
}

function register(options, callback) {
    // checkCapabilities();

    console.log("options: " + JSON.stringify(options, null, 2));

    // https://www.w3.org/TR/webauthn-2/#dictdef-publickeycredentialcreationoptions
    var publicKeyOptions = {

      // Relying Party:
      rp: {
        name: "The Secure Page",
        id: "auth.automatix"  // domain of the site or parent domain (top level not allowed)
      },

      // User:
      user: {
        // id : new Uint8Array([11,31,105,21,31,103,21,31,105,21,1,2,4,5,6,7]),
        // name: "alex.mueller@example.com",
        // displayName: "Alex Müller",
        id: base64ToBuffer(options.user.id),
        name: options.user.name,
        displayName: options.user.fullName,
      },

      // The challenge is produced by the server; see the Security Considerations
      challenge: base64ToBuffer(options.challenge),

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
      authenticatorSelection: {
        // Try to use UV if possible. This is also the default. (e.g. Key needs to be touched)
        userVerification: options.userVerification,
        // client-side discoverable credential (vs server-side generated key)
//        residentKey: "required",
        requireResidentKey: options.residentKey
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

      attestation: options.attestation,

      // Make excludeCredentials check backwards compatible with credentials registered with U2F
      //extensions: {"appidExclude": "https://acme.example.com"}
      // extensions: {txAuthSimple: ""}
      // https://www.w3.org/2019/01/webauthn-extensions.html
      extensions: options.extensions
    };

    console.log("PublicKeyCredentialCreationOptions: " + JSON.stringify(publicKeyOptions, null, 2));
    console.log(publicKeyOptions);

    // Note: The following call will cause the authenticator to display UI.
    navigator.credentials.create({ "publicKey": publicKeyOptions })
        .then(
      function (newCredentialInfo) {
        // Send new credential info to server for verification and registration.
        console.log("new credentials: ")
        console.log(newCredentialInfo);
        console.log(newCredentialInfo.id);
        console.log(bufferToBase64(newCredentialInfo.rawId));
        var credential = {
            "id": newCredentialInfo.id,
            "response": {
                "attestationObject": bufferToBase64(newCredentialInfo.response.attestationObject),
                "clientDataJSON": bufferToBase64(newCredentialInfo.response.clientDataJSON)
            }
        }
        if (newCredentialInfo.getClientExtensionResults !== undefined) {
            console.log("getClientExtensionResults: ")
            console.log(newCredentialInfo.getClientExtensionResults());
        }
        callback(credential);
      }
      ).catch(function (err) {
        // No acceptable authenticator or user refused consent. Handle appropriately.
        alert(err);
      });
}


function login(options, callback) {

    // checkCapabilities();
    // navigator.credentials.preventSilentAccess();

    console.log("options: " + JSON.stringify(options, null, 2));

    var publicKeyCredentialRequestOptions;
    if (options.allowCredentials !== undefined) {
        options.allowCredentials.forEach(function (listItem) {
            listItem.id = base64ToBuffer(listItem.id)
        });
        publicKeyCredentialRequestOptions = {
            challenge: base64ToBuffer(options.challenge),
            allowCredentials: options.allowCredentials,
            userVerification: options.userVerification,
            extensions: options.extensions
          //  timeout: 60000,
        };
    } else  {
        publicKeyCredentialRequestOptions = {
            challenge: base64ToBuffer(options.challenge),
            userVerification: options.userVerification,
            extensions: options.extensions
          //  timeout: 60000,
        };
    }

    console.log("publicKeyCredentialRequestOptions: ")
    console.log(publicKeyCredentialRequestOptions);

    navigator.credentials.get({ "publicKey": publicKeyCredentialRequestOptions, "mediation": "optional" })
      .then(
      function (credential) {
        // Send new credential info to server for verification and registration.
        console.log("credential: ")
        console.log(credential);
        var assertion = {
            "id": credential.id,
            "response": {
                "authenticatorData": bufferToBase64(credential.response.authenticatorData),
                "clientDataJSON": bufferToBase64(credential.response.clientDataJSON),
                "signature":  bufferToBase64(credential.response.signature),
                "userHandle": bufferToBase64(credential.response.userHandle),
            }
        }
        console.log("assertion: " + JSON.stringify(assertion, null, 2));
        if (credential.getClientExtensionResults !== undefined) {
            console.log("getClientExtensionResults: ")
            console.log(credential.getClientExtensionResults());
        }
        callback(assertion);
      }
    ).catch(function (err) {
             alert(err);
           });
}
