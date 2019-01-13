import React, { Component } from 'react';
import './RatingElement.css';

class RatingElement extends Component {

  render() {
    return (
      <div className="RatingElement"
        style={{
          height: window.screen.width / 6, width: window.screen.width / 6,
          backgroundImage: this.props.element === '' ? null : `url(${this.props.element.attributes.image})`,
        }}>
        {this.props.element.name}
      </div>
    );
  }
}

export default RatingElement;
