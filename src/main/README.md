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