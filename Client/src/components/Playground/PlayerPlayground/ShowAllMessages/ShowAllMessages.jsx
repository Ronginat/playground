import React, { Component } from 'react';
import './ShowAllMessages.css';
import axios from 'axios';

const size = 7;
class ShowAllMessages extends Component {
  constructor(props) {
    super(props);
    console.log('in showallmessages');
    this.state = {
      playground: 'ratingplayground',
      messages: [],
      messageToAdd: '',
      page: 0,
    };
    this.getAllMessages();
  }

  getAllMessages = (page) => {
    const newPage = page === undefined ? this.state.page : page;
    const loginUrl = `/activities/${this.props.user.playground}/${this.props.user.email}`;
    const baseUrl = process.env.REACT_APP_API_URL;
    axios({
      url: loginUrl,
      baseURL: baseUrl,
      method: 'POST',
      data: {
        playground: this.state.playground,
        elementPlayground: this.props.element.playground,
        elementId: this.props.element.id,
        type: 'ReadMessages',
        playerPlayground: this.props.user.playground,
        playerEmail: this.props.user.email,
        attributes: {
          page: newPage,
          size: size
        }
      }
    })
      .then(res => res.data)
      .then(res => {
        console.log('all messages', res);
        console.log('all messages2', res.attributes.messages);
        const messageArr = res.attributes.messages.map(m => {
          return m.attributes;
        })
        this.setState({
          messages: messageArr,
          page: newPage
        })
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

  postMessage = () => {
    const loginUrl = `/activities/${this.props.user.playground}/${this.props.user.email}`;
    const baseUrl = process.env.REACT_APP_API_URL;
    axios({
      url: loginUrl,
      baseURL: baseUrl,
      method: 'POST',
      data: {
        playground: this.state.playground,
        elementPlayground: this.props.element.playground,
        elementId: this.props.element.id,
        type: 'PostMessage',
        playerPlayground: this.props.user.playground,
        playerEmail: this.props.user.email,
        attributes: {
          username: this.props.user.username,
          message: this.state.messageToAdd
        }
      }
    })
      .then(res => res.data)
      .then(res => {
        console.log('messages to add', res);
        this.setState({
          messages: [
            ...this.state.messages,
            res.attributes
          ],
          messageToAdd: ""
        })
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

  nextClick = () => {
    if (this.state.messages.length >= size) {
      this.getAllMessages(this.state.page + 1);
    } else {
      alert("last page");
    }
  };

  prevClick = () => {
    if (this.state.page > 0) {
      this.getAllMessages(this.state.page - 1);
    } else {
      alert("first page");
    }
  };


  render() {
    return (
      <div className="ShowAllMessages">
        Messages of {this.props.element.name}
        <br />
        <button className="cancelBtn" onClick={() => this.props.messageBoardAction()}>Back To Elements</button>
        <br />
        {this.state.messages.map(m => {
          return (
            <div className="message-wrapper" key={m.date}>
              <h4>{m.username} -- {m.date}</h4>
              <h5>{m.message}</h5>
            </div>)
        })}
        <br />
        <button onClick={() => this.prevClick()}>Previous</button>
        <button onClick={() => this.nextClick()}>Next</button>
        <br />
        <div className="form">
          <label>New Message:</label>
          <input type="text" placeholder="Enter message" name="message" required value={this.state.messageToAdd} onChange={event => this.setState({ messageToAdd: event.target.value })} />
          <br />
        </div>
        <button onClick={() => this.postMessage()}>Post</button>
      </div>
    );
  }
}

export default ShowAllMessages;
