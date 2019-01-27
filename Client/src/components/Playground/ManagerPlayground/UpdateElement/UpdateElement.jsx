import React, { Component } from 'react';
import './UpdateElement.css';
import axios from 'axios';

class UpdateElement extends Component {
  constructor(props) {
    super(props);
    this.state = {
      type: this.props.element.type,
      name: this.props.element.name,
      expirationDate: this.props.element.expirationDate,
      image: this.props.element.attributes.image,
      info: this.props.element.attributes.info,
    };
  }

  updateElement = () => {
    const loginUrl = `/elements/${this.props.user.playground}/${this.props.user.email}/${this.props.element.playground}/${this.props.element.id}`;
    const baseUrl = process.env.REACT_APP_API_URL;
    axios({
      url: loginUrl,
      baseURL: baseUrl,
      method: 'PUT',
      data: {
        playground: this.state.playground,
        id: this.props.element.id,
        location: {
          x: 0,
          y: 0,
        },
        type: this.state.type,
        name: this.state.name,
        expirationDate: this.state.expirationDate,
        attributes: {
          image: this.state.image,
          info: this.state.info
        }
      }
    })
      .then(res => res.data)
      .then(res => {
        this.props.changeUpdateElementFlag();
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
      <div className="UpdateElement">
        <p>Element Modification Form</p>
        <div className="form">
          {/* <label>Type</label>
          <input type="text" placeholder="Enter type" name="type" required defaultValue={this.state.type} onChange={event => this.setState({ type: event.target.value })} />
          <br /> */}
          <label>Name</label>
          <input type="text" placeholder="Enter name" name="name" required defaultValue={this.state.name} onChange={event => this.setState({ name: event.target.value })} />
          <br />
          <label>Expiration Date</label>
          <input type="date" placeholder="Enter expiration Date" name="expirationDate" required defaultValue={this.state.expirationDate} onChange={event => this.setState({ expirationDate: event.target.value })} />
          <div className="typeRadios">
            <label>Type:</label>
            <h6>MessageBoard</h6>
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
          <input type="text" placeholder="Enter image" name="image" required defaultValue={this.state.image} onChange={event => this.setState({ image: event.target.value })} />
          <br />
          <label>Info</label>
          <input type="text" placeholder="Enter info" name="info" required defaultValue={this.state.info} onChange={event => this.setState({ info: event.target.value })} />
        </div>
        <br />
        <button onClick={() => this.updateElement()}>Update</button>
        <button className="cancelBtn" onClick={() => this.props.changeUpdateElementFlag()}>Cancel</button>
      </div>
    );
  }
}

export default UpdateElement;
