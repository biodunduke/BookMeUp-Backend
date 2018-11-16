const db = require('../config/config');

module.exports = {
    getBook : function(req, res){
      res.send("getBook");
    },
    postBook : function(req, res){
      let userId = req.body.userId;
      let bookName = req.body.bookName;
      let bookAuthor = req.body.bookAuthor;
      var booksRef = db.collection(userId).doc('books_added');
      var addBook = booksRef.set({
        bookName: bookName,
        bookAuthor: bookAuthor,
        userId: userId
      });
      res.send("Book added successfully");
    },
    updateBook : function(req, res){
      res.send("getBook");
    },
}
