import React, { Component } from 'react';
import './Code.css';
import axios from 'axios';

const defaultPlayground = 'ratingplayground';

class Code extends Component {
  constructor(props) {
    super(props);
    this.state = {
      playground: 'ratingplayground',
      email: '',
      code: null
    };
  }

  code = () => {
    const loginUrl = `/users/confirm/${this.state.playground}/${this.state.email}/${this.state.code}`;
    const baseUrl = process.env.REACT_APP_API_URL;
    axios({
      url: loginUrl,
      baseURL: baseUrl,
      method: 'GET',
    })
      .then(res => res.data)
      .then(user => {
        this.props.changeStep('login');

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
      <div className="Code">
        <h1>Code</h1>
        <br />
        <div className="form">
          <label>Email</label>
          <input type="email" placeholder="Enter email" name="email" required onChange={event => this.setState({ email: event.target.value })} />
          <br />
          <label>Playground</label>
          <input type="text" placeholder="Enter playground" name="playground" required defaultValue={defaultPlayground} onChange={event => this.setState({ playground: event.target.value })} />
          <br />
          <label>Code</label>
          <input type="number" placeholder="Enter code" name="code" required onChange={event => this.setState({ code: event.target.value })} />
        </div>
        <br />

        <button onClick={() => this.code()}>code</button>
      </div>
    );
  }
}

export default Code;
