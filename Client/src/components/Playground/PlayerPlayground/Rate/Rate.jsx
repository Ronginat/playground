import React, { Component } from 'react';
import './Rate.css';
import axios from 'axios';

class Rate extends Component {
  constructor(props) {
    super(props);
    this.state = {
      playground: 'ratingplayground',
      myRate: null,
      returnedValue: null,
      hasRated: false,
      watchedAverage: false,
    };
  }

  rate = () => {
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
        type: 'Rating',
        playerPlayground: this.props.user.playground,
        playerEmail: this.props.user.email,
        attributes: {
          rating: this.state.myRate,
        }
      }
    })
      .then(res => res.data)
      .then(res => {
        //console.log('returned value - ', res);
        this.setState({
          returnedValue: res.attributes.content,
          hasRated: true,
        })
        this.props.updatePoints(res.attributes.content.Points);
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

  showRating = () => {
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
        type: 'ShowRating',
        playerPlayground: this.props.user.playground,
        playerEmail: this.props.user.email,
      }
    })
      .then(res => res.data)
      .then(res => {
        //console.log('returned value - ', res);
        this.setState({
          returnedValue: res.attributes.content,
          watchedAverage: true,
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

  render() {
    let moreInfo;
    if (this.props.element.attributes.hasOwnProperty('info')) {
      moreInfo =
        <div className="moreInfo">
          <a href={this.props.element.attributes.info} target="_blank">More Info</a>
          <br />
        </div>
    }
    let component;
    if (!this.state.hasRated && !this.state.watchedAverage) {
      component =
        <div className="rateForm">
          <label>Rating:</label>
          <input type="number" placeholder="Enter your rating" name="rate" required onChange={event => this.setState({ myRate: event.target.value })} />
          <div>
            <button onClick={() => this.rate()}>Rate</button>
            <button onClick={() => this.showRating()}>Show Rating</button>
          </div>
        </div>
    } else if (this.state.hasRated) {
      component =
        <div className="rateForm">
          <h3>The average is {this.state.returnedValue.Average}</h3>
          <br />
          <h4>Your score is {this.state.returnedValue.Points}</h4>
          <br />
          {moreInfo}
        </div>
    } else if (this.state.watchedAverage) {
      component =
        <div className="rateForm">
          <h3>The average is {this.state.returnedValue.Average}</h3>
          <br />
          <h4>Your rating is {this.state.returnedValue.Rating}</h4>
          <br />
          {moreInfo}
        </div>
    }

    return (
      <div className="Rate">
        <p>Rating of {this.props.element.name}</p>
        {component}
        <button className="cancelBtn" onClick={() => this.props.ratingAction()}>Back to elements</button>
      </div>
    );
  }
}

export default Rate;
