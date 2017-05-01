# CercaTrova-Personnel
Design Lab project
- Repository for emergence personnel side application

## Important Links
- [Server Repository](https://github.com/NilanjanDaw/CercaTrova-Server)
- [Emergency User Application Repository](https://github.com/NilanjanDaw/CercaTrova-Client)
- [Emergency Personnel Application Repository](https://github.com/NilanjanDaw/CercaTrova-Personnel)
- [CercaTrova Documentation](https://github.com/NilanjanDaw/CercaTrova-Documentation)

## Setup Instructions
Please follow the following Setup Instructions carefully to correctly run the Personnel side App
### To compile from source
- download the project source Repository.
```bash
git pull https://github.com/NilanjanDaw/CercaTrova-Personnel.git
```
- Open project in Android Studio. Download libraries as required. Build and deploy.

### Running using Binaries
- download latest release version
- Install in target device.
<b> P.S. Since the apps are still in pre-release, enable debug mode in the target device.

### New Personnel Registration
To register a new personnel run the following cURL command in bash:
```bash
curl -X POST \
  http://127.0.0.1:8000/personnel_login_server/user/ \
  -H 'cache-control: no-cache' \
  -H 'content-type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW' \
  -F personnel_id=<personnel_id> \
  -F adhaar_number=<adhaar_number> \
  -F first_name=<first_name> \
  -F last_name=<last_name> \
  -F contact_number=<contact_number> \
  -F car_number=<car_number> \
  -F responder_type=<responder_type> \
  -F base_station=<base_station> \
  -F password=<password> \
  -F 'location=POINT(<latitude> <longitude>)'
```
