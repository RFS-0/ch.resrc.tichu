import { IntendedUserDto, User } from '@/domain/entities/user';
import { FindOrCreateUser } from '@/endpoints/endpoints';
import axios from 'axios';

export class FindOrCreateUserRest implements FindOrCreateUser {
  constructor(private url: string) {
  }

  send(intendedUser: IntendedUserDto): Promise<User> {
    return new Promise((resolve, reject) => {
        axios.post<any>(this.url, intendedUser, {responseType: 'json'}).then(
          response => resolve(response.data),
          () => reject(new Error('Could not fetch user.')),
        );
      },
    );
  }
}
