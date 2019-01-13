import React, { Component } from 'react';
import './UpdateUser.css';
import axios from 'axios';

const defaultPlayground = 'ratingplayground';

class UpdateUser extends Component {
  constructor(props) {
    super(props);
    this.state = {
      userName: this.props.user.username,
      //avatar: '',
      role: this.props.user.role
    };
  }

  update = () => {
    const loginUrl = `/users/${defaultPlayground}/${this.props.user.email}`;
    const baseUrl = process.env.REACT_APP_API_URL;
    axios({
      url: loginUrl,
      baseURL: baseUrl,
      method: 'PUT',
      data: {
        playground: defaultPlayground,
        email: this.props.user.email,
        username: this.state.userName,
        //avatar: this.state.avatar,
        role: this.state.role
      }
    })
      .then(res => res.data)
      .then(user => {
        this.props.updateProfile();

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
      <div className="UpdateUser">
        Update User
        <br />
        <div className="form">
          <label>Username</label>
          <input type="text" placeholder="Enter user name" name="userName" required defaultValue={this.props.user.username} required onChange={event => this.setState({ userName: event.target.value })} />
          <br />
          {/* <label>Avatar</label>
          <input type="text" placeholder="Enter avatar" name="avatar" required defaultValue={this.props.user.avatar} required onChange={event => this.setState({ avatar: event.target.value })} />
          <br /> */}
          {/* <label>Role</label> */}
          {/* <input type="text" placeholder="Enter role" name="role" required defaultValue={this.props.user.role} required onChange={event => this.setState({ role: event.target.value })} /> */}
          <div className="roleRadios">
            <h6>Role:</h6>
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

        <button onClick={() => this.update()}>update</button>
        <button className="cancelBtn" onClick={() => this.props.updateProfileFlag()}>cancel</button>
      </div>
    );
  }
}

export default UpdateUser;
