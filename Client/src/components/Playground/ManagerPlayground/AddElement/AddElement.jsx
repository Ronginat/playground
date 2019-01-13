import React, { Component } from 'react';
import './AddElement.css';
import axios from 'axios';

class AddElement extends Component {
  constructor(props) {
    super(props);
    this.state = {
      playground: 'ratingplayground',
      type: 'MessageBoard',
      name: '',
      expirationDate: '',
      creatorEmail: this.props.user.email,
      creatorPlayground: this.props.user.playground,
      image: '',
      info: '',
    };
  }

  addElement = () => {
    const loginUrl = `/elements/${this.state.creatorPlayground}/${this.state.creatorEmail}`;
    const baseUrl = process.env.REACT_APP_API_URL;
    axios({
      url: loginUrl,
      baseURL: baseUrl,
      method: 'POST',
      data: {
        playground: this.state.playground,
        location: {
          x: 0,
          y: 0,
        },
        type: this.state.type,
        name: this.state.name,
        expirationDate: this.state.expirationDate,
        creatorPlayground: this.state.creatorPlayground,
        creatorEmail: this.state.creatorEmail,
        attributes: {
          image: this.state.image,
          info: this.state.info
        }
      }
    })
      .then(res => res.data)
      .then(res => {
        this.props.changeAddElementFlag();
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
      type: changeEvent.target.value
    });
  }


  render() {
    return (
      <div className="AddElement">
        <p>Element Creation Form</p>
        <div className="form">
          {/* <label>Type</label>
          <input type="text" placeholder="Enter type" name="type" required onChange={event => this.setState({ type: event.target.value })} />
          <br /> */}
          <label>Name</label>
          <input type="text" placeholder="Enter name" name="name" required onChange={event => this.setState({ name: event.target.value })} />
          <br />
          <label>Expiration Date</label>
          <input type="date" placeholder="Enter expiration Date" name="expirationDate" required onChange={event => this.setState({ expirationDate: event.target.value })} />
          <div className="typeRadios">
            <label>Type:</label>
            <h6>Message Board</h6>
            <input className="radio" type="radio" value="MessageBoard"
              checked={this.state.type === "MessageBoard"}
              onChange={this.handleOptionChange} />
            <h6>Movie</h6>
            <input className="radio" type="radio" value="movie"
              checked={this.state.type === "movie"}
              onChange={this.handleOptionChange} />
            <h6>Book</h6>
            <input className="radio" type="radio" value="book"
              checked={this.state.type === "book"}
              onChange={this.handleOptionChange} />
          </div>
          <br />
          <label>Image</label>
          <input type="text" placeholder="Enter image" name="image" required onChange={event => this.setState({ image: event.target.value })} />
          <br />
          <label>Info</label>
          <input type="text" placeholder="Enter info" name="info" required onChange={event => this.setState({ info: event.target.value })} />
        </div>
        <button onClick={() => this.addElement()}>Create</button>
        <button className="cancelBtn" onClick={() => this.props.changeAddElementFlag()}>Cancel</button>
      </div>
    );
  }
}

export default AddElement;
