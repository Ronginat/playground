import React, { Component } from 'react';
import './App.css';
import Authentication from '../Authentication/Authentication';
import Playground from '../Playground/Playground';
import UpdateUser from '../UpdateUser/UpdateUser';


class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      updateProfile: false,
      isLoggedIn: false,
      user: null
    };
  }

  userLoggedIn = (user) => {
    this.setState({
      user,
      isLoggedIn: true
    });
  }

  profileUpdated = () => {
    this.setState({
      updateProfile: false,
      isLoggedIn: false
    })
  }

  updateProfileFlag = () => {
    this.setState({
      updateProfile: !this.state.updateProfile
    })
  }

  logout = () => {
    this.setState({
      isLoggedIn: false,
    })
  }

  render() {
    console.log(this.state.user);

    var component = null;
    if (this.state.updateProfile) {
      component = <UpdateUser user = {this.state.user} updateProfile={this.profileUpdated} updateProfileFlag={this.updateProfileFlag} />
    }
    else if (this.state.isLoggedIn === false) {
      component = <Authentication userLoggedIn={this.userLoggedIn} />;
    } else {
      component = <Playground updateProfileFlag={this.updateProfileFlag} user={this.state.user} logout={this.logout} />;
    }

    return (
      <div className="App">
        <img alt="Playground" src="https://github.com/RanWeiner/PlaygroundClient/blob/master/app/src/main/res/drawable/logo.png?raw=true"></img>
        <br />
        {component}
      </div>
    );
  }
}

export default App;
