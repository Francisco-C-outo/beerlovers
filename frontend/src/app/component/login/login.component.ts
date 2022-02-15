import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';

const AUTH_API = 'http://localhost:8080/v1/login';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  
  form: any = {
    username: null,
    password: null
  };
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = '';

  constructor(private router: Router, private http: HttpClient) { }

  ngOnInit(): void {
    sessionStorage.setItem('token', '');
  }

  onSubmit(): void {
        this.http.post<Observable<boolean>>(AUTH_API, {
            userName: this.form.username,
            password: this.form.password
        }).subscribe(isValid => {
            if (isValid) {
              sessionStorage.setItem('token', btoa(this.form.username + ':' + this.form.password));
              this.isLoginFailed = false;
              this.isLoggedIn = true;
              this.router.navigateByUrl('/beers');
            } else {
              this.errorMessage = "Invalid credentials were provided!"
              this.isLoginFailed = true;
            }
        });
  }
  
  reloadPage(): void {
    window.location.reload();
  }
}
