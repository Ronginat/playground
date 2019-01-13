import React, { Component } from 'react';
import './Register.css';
import axios from 'axios';

const defaultPlayground = 'ratingplayground';

class Register extends Component {
  constructor(props) {
    super(props);
    this.state = {
      playground: 'ratingplayground',
      email: '',
      userName: '',
      //avatar: '',
      role: 'player'
    };
  }

  register = () => {
    const loginUrl = '/users';
    const baseUrl = process.env.REACT_APP_API_URL;
    axios({
      url: loginUrl,
      baseURL: baseUrl,
      method: 'POST',
      data: {
        playground: this.state.playground,
        email: this.state.email,
        userName: this.state.userName,
        //avatar: this.state.avatar,
        role: this.state.role
      }
    })
      .then(res => res.data)
      .then(user => {
        this.props.changeStep('code');

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

  handleOptionChange = (changeEvent) => {
    this.setState({
      role: changeEvent.target.value
    });
  }

  render() {
    return (
      <div className="Register">
        <h1>Register</h1>
        <br />
        <div className="form">
          <label>Email</label>
          <input type="email" placeholder="Enter email" name="email" required onChange={event => this.setState({ email: event.target.value })} />
          <br />
          <label>Playground</label>
          <input type="text" placeholder="Enter playground" name="playground" required defaultValue={defaultPlayground} onChange={event => this.setState({ playground: event.target.value })} />
          <br />
          <label>Username</label>
          <input type="text" placeholder="Enter user name" name="userName" required onChange={event => this.setState({ userName: event.target.value })} />
          <br />
          {/* <label>Avatar</label>
          <input type="text" placeholder="Enter avatar" name="avatar" required onChange={event => this.setState({ avatar: event.target.value })} />
          <br /> */}
          {/* <label>Role</label>
          <input type="text" placeholder="Enter role" name="role" required onChange={event => this.setState({ role: event.target.value })} /> */}

        </div>
        <div className="form2">
          <div className="roleRadios">
            <label>Role:</label>
            <h6>Player</h6>
            <input className="radio" type="radio" value="player"
              checked={this.state.role === "player"}
              onChange={this.handleOptionChange} />
            <h6>Manager</h6>
            <input className="radio" type="radio" value="manager"
              checked={this.state.role === "manager"}
              onChange={this.handleOptionChange} />
          </div>
        </div>
        <br />

        <button onClick={() => this.register()}>Register</button>
      </div>
    );
  }
}

export default Register;