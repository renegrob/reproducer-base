# WebAuthn / FIDO2 / FIDO U2F

## How it works

https://www.w3.org/TR/webauthn-2/

https://developer.mozilla.org/en-US/docs/Web/API/Web_Authentication_API

https://developers.google.com/web/updates/2018/05/webauthn

https://webauthn.guide/

https://developers.yubico.com/WebAuthn/WebAuthn_Developer_Guide/Resident_Keys.html

https://codelabs.developers.google.com/codelabs/webauthn-reauth#0

https://github.com/StrongKey/fido2

https://github.com/StrongKey/fido2/blob/master/server/api/src/main/java/com/strongkey/apiws/rest/APIServlet.java

https://duo.com/labs/tech-notes/resident-keys-and-the-future-of-webauthn-fido2

https://www.iana.org/assignments/cose/cose.xhtml#algorithms

## Resident Keys / Discoverable Credential

https://webauthn-doc.spomky-labs.com/deep-into-the-framework/authentication-without-username

https://auth0.com/blog/a-look-at-webauthn-resident-credentials/


For passwordless authentication uses cases, security keys must support two features.

resident key
user verification (PIN or embedded sensor like Fingerprint scanner)
Some security keys support user verification by PIN code (Yubikey5), and some by embedded sensor (Feitian BioPass2).
Not sure all the FIDO2 security keys support resident key feature, but at least Yubikey5 supports it.
So Yubikey5 can be used for passwordless authentication scenario (but you need to provide PIN during authentication instead of password).

Any subsequent authentication request with that relying party will use the saved resident credential to find the corresponding credential private key unless an array of allowed credentials is configured in the navigator.credentials.get() options.

## Extensions

* https://www.w3.org/2019/01/webauthn-extensions.html
* https://developer.mozilla.org/en-US/docs/Web/API/PublicKeyCredentialRequestOptions/extensions

````json
extensions: {
    uvm: true,
    loc: false,
    txAuthSimple: "Could you please verify yourself?"
}
````

## Level 2

https://developers.yubico.com/WebAuthn/Concepts/WebAuthn_Level_2_Features_and_Enhancements.html

https://www.w3.org/TR/webauthn-2/

https://services.w3.org/htmldiff?doc1=https%3A%2F%2Fwww.w3.org%2FTR%2Fwebauthn-1&doc2=https%3A%2F%2Fw3c.github.io%2Fwebauthn%2F


## Certification

https://fidoalliance.org/specs/fido-v2.0-rd-20180702/fido-server-v2.0-rd-20180702.html

http://fidoalliance.org/wp-content/uploads/2019/04/Functional_Certification_Program_Policy_v1.3.7_09252018_FINAL_Ammended-1.pdf

https://fidoalliance.org/certification/interoperability-testing/

## Endpoints

### IBM:
* https://[wrp_hostname]/[junction]/sps/fido2/[rp_config_id]/attestation/options
* https://[wrp_hostname]/[junction]/sps/fido2/[rp_config_id]/attestation/result
* https://[wrp_hostname]/[junction]/sps/fido2/[rp_config_id]/assertion/options
* https://[wrp_hostname]/[junction]/sps/fido2/[rp_config_id]/assertion/result