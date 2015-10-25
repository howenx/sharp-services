package controllers;

import domain.Vuser;
import play.Logger;
import play.mvc.*;

import service.H2Service;
import service.PostgresqlService;
import views.html.*;

import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.Valid;
import play.data.*;

public class Application extends Controller {

    @Named("ServiceH2")
    @Inject
    private H2Service h2Service;

    @Inject
    private PostgresqlService postgresqlService;


    public Result listUsers(String lang) {
        return ok(users.render(h2Service.getAllUser(),lang));
    }

    public Result listBrands(String lang) {
        return ok(brands.render(postgresqlService.getAllBrand(),lang));
    }

    public Result validate(String lang) {
        //        if (user.validate()!=null){
        //
        //        }
        //        Form<Vuser> userForm = Form.form(Vuser.class);
        //        Vuser user = userForm.bindFromRequest().get();
        return ok(validate.render(lang));
    }

    public Result validation(){
        return ok("success");
    }
}
