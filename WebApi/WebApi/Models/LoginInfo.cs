using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WebApi_JWT.Models
{
    public class LoginInfo
    {
        public string UserID { get; set; }
        public string Password { get; set; }
    }

    public class LoginResult
    {
        public bool result { get; set; }
        public string rtntoken { get; set; }
    }
}