const express = require('express');
const bodyParser = require('body-parser');
const app = express();

var routes = require('./routes/routes');

app.use(bodyParser.urlencoded({ extended: true }))
app.use(bodyParser.json())

app.use('/api',routes);

app.listen(3000, () => {
    console.log("Server is listening on port 3000");
});
