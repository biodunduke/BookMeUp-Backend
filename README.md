Official repository for the BookMeUp project.

Notes:

Dependencies
--------------
NodeJS
ngrok (secure tunneling to localhost)

Running the backend
---------------------
cd backend
npm install
nodemon index.js
ngrok http 3000

Then, copy the ngrok generated URL to the api string in the activities/fragments that are 
making api calls

