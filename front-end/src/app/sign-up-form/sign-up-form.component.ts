import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { catchError, throwError } from 'rxjs';

@Component({
  selector: 'sign-up-form',
  standalone: true,
  imports: [FormsModule, HttpClientModule],  // Import HttpClientModule here
  templateUrl: './sign-up-form.component.html',
  styleUrls: ['./sign-up-form.component.scss']
})
export class SignUpFormComponent implements OnInit {
  nationalId: string = "";
  name: string = "";
  family: string = "";
  phoneNumber: string = "";
  dateOfBirth: string = "";
  isMail: boolean = true;
  militaryServiceStatus: string = "";
  email: string = "";

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
  }

  onSubmit(): void {
    const signUpData = {
      nationalId: this.nationalId,
      name: this.name,
      family: this.family,
      phoneNumber: this.phoneNumber,
      dateOfBirth: this.dateOfBirth,
      isMail: this.isMail,
      militaryServiceStatus: this.militaryServiceStatus,
      email: this.email
    };

    this.http.post('http://127.0.0.1:8081/api/v1/person/signup', signUpData)
    .pipe(
      catchError(error => {
        console.error('Error occurred:', error);
        return throwError(error);
      })
    )
    .subscribe(response => {
      console.log('Success:', response);
    });
  }
}
