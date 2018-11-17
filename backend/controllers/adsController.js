const db = require('../config/config');

module.exports = {
    getAd : function(req, res){
      res.send("getBook");
    },
    getAds : function(req, res){
      var adsRef = db.collection('users').doc(req.query.userId).collection('adsCreated');
      var payload = {};
      var data = []
      var allAds = adsRef.get()
          .then(snapshot => {
            snapshot.forEach(doc => {
              data.push(doc.data())
            });
            res.send(data)
          })
          .catch(err => {
            console.log('Error getting documents', err);
          });

    },
    postAd : function(req, res){
      let userId = req.body.userId;
      let bookName = req.body.bookName;
      let bookAuthor = req.body.bookAuthor;
      let bookPrice = req.body.bookPrice;
      let bookCondition = req.body.bookCondition;
      let bookPicUrl = req.body.bookPicUrl;
      var adsDoc = db.collection('users').doc(userId).collection('adsCreated').add({
        bookName: bookName,
        bookAuthor: bookAuthor,
        bookPrice: bookPrice,
        bookCondition:bookCondition,
        bookPicUrl:bookPicUrl
      }).then(ref => {
        console.log('Added document with ID: ', ref.id);
        res.send("Ad posted successfully");
      });
    },
    updateAd : function(req, res){
      res.send("getBook");
    },
    promoteAd : function(req, res){
      res.send("getBook");
    },
    deleteAd : function(req, res){
      res.send("getBook");
    },
}
