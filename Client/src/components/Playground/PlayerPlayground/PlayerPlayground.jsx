import React, { Component } from 'react';
import './PlayerPlayground.css';
import MessageBoardElement from '../../MessageBoardElement/MessageBoardElement';
import RatingElement from '../../RatingElement/RatingElement';
import ShowAllMessages from './ShowAllMessages/ShowAllMessages';
import Rate from './Rate/Rate';

class PlayerPlayground extends Component {
  constructor(props) {
    super(props);
    this.state = {
      // clicked on MessageBoard element
      messageBoardActionFlag: false,
      // clicked on Rating element
      ratingActionFlag: false,
      // which element to activate
      elementToAction: null,
      points: this.props.user.points,
    };
  }

  messageBoardAction = (element) => {
    if (this.state.messageBoardActionFlag) {
      this.props.getAllElements();
      this.props.isInsideElementPage(true);
      this.setState({ elementToAction: null })
    } else {
      this.props.isInsideElementPage(false);
      this.setState({ elementToAction: element })
    }
    this.setState({ messageBoardActionFlag: !this.state.messageBoardActionFlag })
  }

  ratingAction = (element) => {
    if (this.state.ratingActionFlag) {
      this.props.getAllElements();
      this.props.isInsideElementPage(true);
      this.setState({ elementToAction: null })
    } else {
      this.props.isInsideElementPage(false);
      this.setState({ elementToAction: element })
    }
    this.setState({ ratingActionFlag: !this.state.ratingActionFlag })
  }

  updatePoints = (num) => {
    const points = this.state.points + num;
    this.setState({
      points
    })
  }

  render() {
    let component
    if (this.state.messageBoardActionFlag) {
      component = <ShowAllMessages user={this.props.user} element={this.state.elementToAction} messageBoardAction={this.messageBoardAction} />
    } else if (this.state.ratingActionFlag) {
      component = <Rate user={this.props.user} element={this.state.elementToAction} ratingAction={this.ratingAction} updatePoints={this.updatePoints} />
    }
    else {
      component = this.props.elements.map(e => {
        if (e.type === 'MessageBoard') {
          return (
            <div style={{ display: 'inline-block' }} key={e.id} onClick={() => this.messageBoardAction(e)}>
              <MessageBoardElement key={e.id} element={e} />
            </div>
          )
        } else {
          return (
            <div style={{ display: 'inline-block' }} key={e.id} onClick={() => this.ratingAction(e)}>
              <RatingElement key={e.id} element={e} />
            </div>
          )
        }
      });
    }

    return (
      <div className="PlayerPlayground">
        <p>Welcome {this.props.user.username} || Score: {this.state.points}</p>
        <br />
        {component}
      </div>
    );
  }
}

export default PlayerPlayground;
