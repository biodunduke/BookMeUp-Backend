const db = require('../config/config');

module.exports = {
    updateUser : function(req, res){
      let userId = req.body.userId;
      let city = req.body.city;
      let country = req.body.country;
      let address = req.body.address;
      var userDoc = db.collection('users').doc(userId).update({
        address: address,
        city: city,
        country: country,
      }).then(ref => {
        console.log('Success!', ref.id);
        res.send("Ad posted successfully");
      });
    },

    getUserData : function(req, res){
      let userId = req.query.userId;
      var data = []
      var userDoc = db.collection('users').doc(userId).get()
         .then(doc => {
           if (!doc.exists) {
             res.send('No such document!');
           } else {
             res.send(doc.data())
           }
         })
         .catch(err => {
           console.log('Error getting document', err);
         });
    },
}
