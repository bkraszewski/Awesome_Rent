#Endpoints description

#Register
POST /api/register
Request:

    {
      "email":"test@wp.pl",
      "password": "test1234"
    }
    
Response: 

    {
      "email":"email@test.pl",
      "id": 1,
      "role": "USER",
      "token": "asdsgdfgd"
    }
    
#Login    
POST /api/login

Request: 

    {
        "email":"test@wp.pl",
        "password": "test1234"
    }
    
Response: 
    
    {
      "email":"email@test.pl",
      "id": 1,
      "role": "USER",
      "token": "asdsgdfgd"
    }