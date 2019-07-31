const express = require('express');
const bodyParser = require('body-parser')
const app = express();
const port = 3000;
"use strict";

const jsonParser = bodyParser.json();
const roles = {user: "USER", realtor: "REALTOR", admin: "ADMIN"}


app.use(bodyParser.json());

const admin = require("firebase-admin");

const serviceAccount = require("./serviceAccountKey.json");

admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
    databaseURL: "https://awesomerent-1564416356607.firebaseio.com"
});

let db = admin.firestore();

app.get('/', (req, res) => {
    res.json({message: 'api is running!'})

});

app.post('/api/register', jsonParser, (req, res) => {

    try {
        console.log(req.body);
        const user = {login: req.body.email, id: 1, role: 'ADMIN', token: "dsdgdgf"};
        res.json(user)
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

app.listen(port, () => console.log(`Example app listening on port ${port}!`));