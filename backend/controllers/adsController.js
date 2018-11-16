const db = require('../config/config');

module.exports = {
    getAd : function(req, res){
      res.send("getBook");
    },
    getAds : function(req, res){
      var adsRef = db.collection('ads');
      var payload = {};
      var data = []
      var allAds = adsRef.get()
          .then(snapshot => {
            snapshot.forEach(doc => {
              payload = doc.data()
              data.push(payload)
            });
            res.send(data);
          })
          .catch(err => {
            console.log('Error getting documents', err);
          });

    },
    postAd : function(req, res){
      let posterId = req.body.posterId;
      let bookName = req.body.bookName;
      let bookAuthor = req.body.bookAuthor;
      let bookPrice = req.body.bookPrice;
      let bookCondition = req.body.bookCondition;
      let bookPicUrl = req.body.bookPicUrl;
      var adsDoc = db.collection('ads').add({
        posterId: posterId,
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
