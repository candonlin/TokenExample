using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using WebApi_JWT.Models;
using System.Configuration;

namespace WebApi_JWT.Controllers
{
    [JwtAuthActionFilter]
    public class ValuesController : ApiController
    {
        
        // GET api/values
        [Route("api/values/Get1/ID/{ID}")]
        [HttpGet]
        public IHttpActionResult Get1(string ID)
        {
            try
            {
                int n;
                if (int.TryParse(ID, out n))
                    return Ok(n + 1);
                else
                    return new System.Web.Http.Results.ResponseMessageResult(
                       Request.CreateErrorResponse(
                       (HttpStatusCode)999,
                       new HttpError("輸入的參數非數字")
                       )
                   );
            }
            catch (Exception ex)
            {

                return new System.Web.Http.Results.ResponseMessageResult(
                        Request.CreateErrorResponse(
                        (HttpStatusCode)999,
                        new HttpError(ex.Message)
                        )
                    );
            }
        }

        // GET api/values
        [Route("api/values/Get2/name/{name}")]
        [HttpGet]
        public IHttpActionResult Get2(string name)
        {
            try
            {
                List<Product> listProduct = new List<Product>();
                listProduct.Add(new Product { Name = "A", No = "1", Qty = 112 });
                listProduct.Add(new Product { Name = "A", No = "2", Qty = 12 });
                listProduct.Add(new Product { Name = "A", No = "3", Qty = 14 });
                listProduct.Add(new Product { Name = "A", No = "4", Qty = 14 });
                listProduct.Add(new Product { Name = "B", No = "1", Qty = 12 });
                listProduct.Add(new Product { Name = "B", No = "2", Qty = 1 });
                listProduct.Add(new Product { Name = "B", No = "3", Qty = 5 });
                listProduct.Add(new Product { Name = "B", No = "4", Qty = 32 });
                listProduct.Add(new Product { Name = "C", No = "1", Qty = 5 });
                listProduct.Add(new Product { Name = "C", No = "2", Qty = 8 });
                listProduct.Add(new Product { Name = "C", No = "3", Qty = 9 });
                listProduct.Add(new Product { Name = "D", No = "1", Qty = 111 });
                listProduct.Add(new Product { Name = "D", No = "2", Qty = 25 });
                listProduct.Add(new Product { Name = "D", No = "3", Qty = 13 });

                listProduct = listProduct.Where(e => e.Name == name.ToUpper()).ToList();
                if (listProduct.Count > 0)
                {
                    return Ok(listProduct);
                }
                else
                {
                    return new System.Web.Http.Results.ResponseMessageResult(
                        Request.CreateErrorResponse(
                        (HttpStatusCode)999,
                        new HttpError("請輸入A到D的範圍")
                        )
                    );
                }
            }
            catch (Exception ex)
            {

                return new System.Web.Http.Results.ResponseMessageResult(
                        Request.CreateErrorResponse(
                        (HttpStatusCode)999,
                        new HttpError(ex.Message)
                        )
                    );
            }
        }


        // POST api/values/id
        [Route("api/values/Post1")]
        [HttpPost]
        public IHttpActionResult Post1([FromBody]UserModel User)
        {
            try
            {
                if(User== null)
                {
                    return new System.Web.Http.Results.ResponseMessageResult(
                                      Request.CreateErrorResponse(
                                      (HttpStatusCode)999,
                                      new HttpError("參數錯誤")
                                      )
                                  );
                }
                if (User.Name == "regal")
                {
                    if (User.Pwd == "89947155")
                    {
                        return Ok(true);
                    }
                }
                return Ok(false);
            }
            catch(Exception ex)
            {
                return new System.Web.Http.Results.ResponseMessageResult(
                                       Request.CreateErrorResponse(
                                       (HttpStatusCode)999,
                                       new HttpError(ex.Message)
                                       )
                                   );
            }
        }

        // POST api/values/id
        [Route("api/values/Post2")]
        [HttpPost]
        public IHttpActionResult Post2([FromBody]List<SUM_Model> Data)
        {
            try
            {

                if (Data == null)
                {
                    return new System.Web.Http.Results.ResponseMessageResult(
                                      Request.CreateErrorResponse(
                                      (HttpStatusCode)999,
                                      new HttpError("參數錯誤")
                                      )
                                  );
                }
                decimal Total = Data.Sum(e => e.Qty);
                return Ok(Total);
            }
            catch (Exception ex)
            {
                return new System.Web.Http.Results.ResponseMessageResult(
                                       Request.CreateErrorResponse(
                                       (HttpStatusCode)999,
                                       new HttpError(ex.Message)
                                       )
                                   );
            }
        }
    }
}
