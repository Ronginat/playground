import React, { Component } from 'react';
import './Login.css';
import axios from 'axios';

const defaultPlayground = 'ratingplayground';

class Login extends Component {
  constructor(props) {
    super(props);
    this.state = {
      playground: defaultPlayground,
      email: '',
    };
  }

  login = () => {
    const loginUrl = `/users/login/${this.state.playground}/${this.state.email}`;
    const baseUrl = process.env.REACT_APP_API_URL;
    axios({
      url: loginUrl,
      baseURL: baseUrl,
      method: 'GET',
    })
      .then(res => res.data)
      .then(user => {
        if (user.code === -1) {
          console.log('confirmed user');
          this.props.userLoggedIn(user);
        } else {
          console.log('waiting for confirm code');
        }
      })
      .catch(e => {
        if (e.response !== undefined
          && e.response.data !== undefined
          && e.response.data.message !== undefined
          && e.response.status === 500) {
          alert(e.response.data.message);
        } else {
          alert(e);
          console.log(e);
        }
      });
  }

  render() {
    return (
      <div className="Login">
        <h1>Login</h1>
        <br />
        <div className="form">
          <label>Email</label>
          <br />
          <input type="email" placeholder="Enter email" name="email" required onChange={event => this.setState({ email: event.target.value })} />
          <br />
          <label>Playground</label>
          <br />
          <input type="text" placeholder="Enter playground" name="playground" required defaultValue={defaultPlayground} onChange={event => this.setState({ playground: event.target.value })} />
          <br />
          <button onClick={() => this.login()}>Login</button>
        </div>
      </div>
    );
  }
}

export default Login;
