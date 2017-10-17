import { Injectable }    from '@angular/core';
import { Headers, Http } from '@angular/http';

import 'rxjs/add/operator/toPromise';

import { loginForm } from '../Models/loginForm';

@Injectable()

export class LoginService {
  private headers = new Headers();
  private loginUrl = 'http://localhost:8080/toLogin';

  public user : loginForm[];

  constructor(
      private http : Http
    ) { }

  login(form: loginForm): Promise<loginForm> {
    let body;
    if (this.check_info(form.info)) {
      body = JSON.stringify({
        email: form.info,
        password: form.password
      });
    }
    else{
      body = JSON.stringify({
        userName: form.info,
        password: form.password
      });
    }

    return this.http
           .post(this.loginUrl, body, { headers: this.headers})
           .toPromise()
           .then(res => res.json().data as loginForm)
           .catch(this.handleError);
  };

  private check_info(info: string) {
    const regPattern = new RegExp("^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$");
    let email : boolean = regPattern.test(info);
    return email;
  }

  private handleError(error: any): Promise<any> {
      console.error('An error occurred', error); // for demo purposes only
      return Promise.reject(error.message || error);
    }
}