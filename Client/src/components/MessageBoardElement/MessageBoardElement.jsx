import React, {
  Component
} from 'react';
import './MessageBoardElement.css';

class MessageBoardElement extends Component {

  render() {

    return (
      <div className="MessageBoardElement"
        style={
          {
            height: window.screen.width / 6,
            width: window.screen.width / 6,
            backgroundImage: this.props.element === '' ? null : `url(${this.props.element.attributes.image})`,
          }
        }
      >
        {this.props.element.name}
      </div>
    );
  }
}

export default MessageBoardElement;