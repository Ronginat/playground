import React, { Component } from 'react';
import './Authentication.css';
import Register from './Register/Register';
import Login from './Login/Login';
import Code from './Code/Code';


class Authentication extends Component {
  constructor(props) {
    super(props);
    this.state = {
      step: 'login',
      playground: 'playground',
      email: 'playerEmail@gmail.com',
    };
  }

  changeStep = (step) => {
    this.setState({
      step
    })
  }

  getClassName = (btn) => {
    if (btn === this.state.step) {
      return 'pushed';
    } else {
      return '';
    }
  }

  render() {
    let toShow;
    if (this.state.step === 'login') {
      toShow = <Login changeStep={this.changeStep} userLoggedIn={this.props.userLoggedIn} />
    }
    else if (this.state.step === 'register') {
      toShow = <Register changeStep={this.changeStep} />
    }
    else if (this.state.step === 'code') {
      toShow = <Code changeStep={this.changeStep} />
    }

    return (
      <div className="Authentication">
        <button className={this.getClassName('login')} onClick={() => this.changeStep('login')}>Login</button>
        <button className={this.getClassName('register')} onClick={() => this.changeStep('register')}>Register</button>
        <button className={this.getClassName('code')} onClick={() => this.changeStep('code')}>Code</button>
        <br></br>
        {toShow}
      </div>
    );
  }
}

export default Authentication;
