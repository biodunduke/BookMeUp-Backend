var express = require('express');
var bookCtrl = require('../controllers/bookController');
var adsCtrl = require('../controllers/adsController');
var userCtrl = require('../controllers/userController');

var router = express.Router();

router.route('/books').get(bookCtrl.getBook);
router.route('/books').post(bookCtrl.postBook);
router.route('/ad').get(adsCtrl.getAd);
router.route('/ads').get(adsCtrl.getAds);
router.route('/ads').post(adsCtrl.postAd);
router.route('/users').post(userCtrl.updateUser);
router.route('/users').get(userCtrl.getUserData);

module.exports = router;
