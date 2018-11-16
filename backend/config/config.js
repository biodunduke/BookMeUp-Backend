const admin = require('firebase-admin');

var serviceAccount = "../../../../Desktop/bookmeup_creds.json";

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount)
});

var db = admin.firestore();

module.exports = db;
