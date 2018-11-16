var express = require('express');
var bookCtrl = require('../controllers/bookController');
var adsCtrl = require('../controllers/adsController');

var router = express.Router();

router.route('/books').get(bookCtrl.getBook);
router.route('/ads').get(adsCtrl.getAds);
router.route('/ads').post(adsCtrl.postAd);
router.route('/books').post(bookCtrl.postBook);

module.exports = router;
