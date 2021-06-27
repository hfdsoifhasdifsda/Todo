import axios from 'axios';
import authHeader from './auth-header';

const API_URL = 'http://localhost:8080/api/todo/';

class TodoService {
  get() {
    return axios.get(API_URL + 'list', { headers: authHeader() });
  }

  post(data) {
    return axios.post(API_URL + 'post', {
      message: data,
    },
    {
      headers: authHeader(),
    });
  }

  delete(data) {
    return axios.post(API_URL + 'delete', {
      message: data,
    },
    {
      headers: authHeader(),
    });
  }
}

export default new TodoService();
