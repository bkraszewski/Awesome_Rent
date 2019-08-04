const express = require('express');
const bodyParser = require('body-parser')
const app = express();
const port = 3000;
"use strict";

const jsonParser = bodyParser.json();
const roles = {user: "USER", realtor: "REALTOR", admin: "ADMIN"}


app.use(bodyParser.json());

const admin = require("firebase-admin");
const firebase = require("firebase");

const serviceAccount = require("./serviceAccountKey.json");

admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
    databaseURL: "https://awesomerent-1564416356607.firebaseio.com"
});

var firebaseConfig = {
    apiKey: "AIzaSyDrtsait4DhptltRHKujvFxya_yO3w0Mkk",
    authDomain: "awesomerent-1564416356607.firebaseapp.com",
    databaseURL: "https://awesomerent-1564416356607.firebaseio.com",
    projectId: "awesomerent-1564416356607",
    storageBucket: "awesomerent-1564416356607.appspot.com",
    messagingSenderId: "70589210095",
    appId: "1:70589210095:web:a1e12027b1d7eb1a"
};
// Initialize Firebase
firebase.initializeApp(firebaseConfig);

let db = admin.firestore();

app.get('/', (req, res) => {
    res.json({message: 'api is running!'})

});

app.get('/api/users', jsonParser, (req, res) => {

    try {

        admin.auth().listUsers().then((listUsersResult) => {
            const users = listUsersResult.users.map(record => {
                console.log(record)
                const user = {}
                user.email = record.email;
                user.role = record.displayName;
                user.id = record.uid
                return user;
            });
            res.json({users: users})
        })
            .catch((error) => {
                console.log(error);
                res.statusCode = 500;
                res.json({error: error})
            })
    } catch (error) {
        console.log(error);
        res.statusCode = 500;
        res.json({error: error})
    }
});

app.post('/api/users', jsonParser, (req, res) => {

    try {

        const user = {};
        user.email = req.body.email;
        user.role = req.body.role;

        admin.auth().createUser({
            email:user.email,
            displayName : user.role
        }).then((record) => {
            user.id = record.uid
            res.json({user: user})
        }).catch((error) => {
            res.statusCode = 500;
            res.json({error: error})
        })


    } catch (error) {
        console.log(error);
        res.statusCode = 500;
        res.json({error: error})
    }
});

app.delete('/api/users/:id', jsonParser, (req, res) => {

    try {
        const id = req.params.id;

        admin.auth().deleteUser(id)
            .then(function() {
                res.statusCode = 200
                res.end()
            })
            .catch(function(error) {
                console.log(error);
                res.statusCode = 500;
                res.json({error: error})
            });


    } catch (error) {
        console.log(error);
        res.statusCode = 500;
        res.json({error: error})
    }
});

app.put('/api/users/:id', jsonParser, (req, res) => {

    try {
        const id = req.params.id;
        const user = {};
        user.email = req.body.email;
        user.role = req.body.role;
        user.id = id;

        admin.auth().updateUser(user.id, {
            email: user.email,
            displayName: user.role,
        })
            .then(function(userRecord) {
                // See the UserRecord reference doc for the contents of userRecord.
                console.log('Successfully updated user', userRecord.toJSON());
                res.statusCode = 200;
                res.end()
            })
            .catch(function(error) {
                console.log(error);
                res.statusCode = 500;
                res.json({error: error})
            });


    } catch (error) {
        console.log(error);
        res.statusCode = 500;
        res.json({error: error})
    }
});



app.listen(port, () => console.log(`Example app listening on port ${port}!`));