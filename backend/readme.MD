#Endpoints description

#List all users
GET /api/users

Request

Response:
    [{
      "email":"email@test.pl",
      "id": 1,
      "role": "USER"
      },]

#create user

Request:
POST /api/users

    {
      "email":"test@wp.pl",
      "role": "test1234"
    }
    
Response: 

    {
      "email":"email@test.pl",
      "id": 1,
      "role": "USER",
    }
  }
  
#edit user
  PUT api/users/:id
  
  Request
  
      {
        "email":"test@wp.pl",
        "role": "test1234"
      }
      
  Response
  no content
      
#delete user
  DELETE api/users/:id
  Request
      
  Response: 
  no content