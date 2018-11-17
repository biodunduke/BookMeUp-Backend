const db = require('../config/config');

module.exports = {
    getAds : function(req, res){
      var colsRef = db.collection('users');
      var booksData = []
      var query = colsRef.get()
        .then(snapshot => {
          snapshot.forEach(doc => {
            var sfRef = colsRef.doc(doc.id);
            sfRef.getCollections().then(collections => {
              collections.forEach(collection => {
                console.log('Found subcollection with id:', collection.id);
                let dataRef = db.collection('users').doc(doc.id).collection(collection.id);
                dataRef.get().then(snapshot =>{
                    snapshot.forEach(book => {
                        booksData.push(book.data())
                        console.log(book.data())
                    })
                    res.send(booksData)
                })
                //console.log(collection.data())
              });
            });
          });
        })
        .catch(err => {
          console.log('Error getting documents', err);
        });
    },
    getAd : function(req, res){
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
      let bookISBN = req.body.bookISBN;
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
