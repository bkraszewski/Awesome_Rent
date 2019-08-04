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

app.post('/api/register', jsonParser, (req, res) => {

    try {
        console.log(req.body);
        const user = {email: req.body.email, password: req.body.password};

        admin.auth().createUser({
            email: user.email,
            emailVerified: false,
            password: user.password,
            displayName: roles.user,
            disabled: false
        }).then(function (userRecord) {
            // See the UserRecord reference doc for the contents of userRecord.
            // console.log('Successfully created new user:', userRecord.uid);
            //
            // firebase.auth().signInWithEmailAndPassword(user.email, user.password)
            //     .then((authUser) => {
            //         console.log('authenticated!')
            //         authUser.user.getIdToken(true).then((token ) => {
            //             console.log('got token: ', token)
            //             res.json({token: token})
            //         }).catch((error => {
            //             console.log('Error authenticating', error);
            //             res.statusCode = 500;
            //             res.json({error: error})
            //         }))
            //
            //
            //     }).catch((error => {
            //     console.log('Error authenticating', error);
            //     res.statusCode = 500;
            //     res.json({error: error})
            // }))

            // userRecord.getIdToken(true)
            //     .then((token) => {
            //         user.token = token
            //         res.json(user)
            // }).catch((error) => {
            //     console.log('Error creating new user:', error);
            //     res.statusCode = 500;
            //     res.json({error: error})
            // })

            admin.auth().createCustomToken(userRecord.uid)
                .then(function (customToken) {
                    // Send token back to client
                    user.token = customToken
                    res.json(user)
                })
                .catch(function (error) {
                    console.log('Error creating custom token:', error);
                    res.statusCode = 500;
                    res.json({error: error})
                });

        })
            .catch(function (error) {
                console.log('Error creating new user:', error);
                res.statusCode = 500;
                res.json({error: error})
            });

    } catch (error) {
        console.log(error);
        res.statusCode = 500;
        res.json({error: error})
    }
});

app.post('/api/login', jsonParser, (req, res) => {

    try {
        let docRef = db.collection('users').doc('alovelace');

        let setAda = docRef.set({
            first: 'Ada',
            last: 'Lovelace',
            born: 1815
        });


        console.log(req.body);
        const user = {login: req.body.email, id: 1, role: 'ADMIN', token: "dsdgdgf"};
        res.json(user)
    } catch (error) {
        console.log(error);
        res.statusCode = 500;
        res.json({error: error})
    }
});

app.get('/api/users', jsonParser, (req, res) => {
    const token = req.body.token;
    admin.auth().verifyIdToken(token)
        .then(function (decodedToken) {
            let uid = decodedToken.uid;
            res.json({message: "authenticated!"})
            // ...
        }).catch(function (error) {

        res.statusCode = 401;
        res.json({error: error});

    });

});

app.listen(port, () => console.log(`Example app listening on port ${port}!`));