import React, { Component } from 'react';
import './ManagerPlayground.css';
import MessageBoardElement from '../../MessageBoardElement/MessageBoardElement';
import RatingElement from '../../RatingElement/RatingElement';
import AddElement from './AddElement/AddElement';
import UpdateElement from './UpdateElement/UpdateElement';

class ManagerPlayground extends Component {
  constructor(props) {
    super(props);
    this.state = {
      addElement: false,
      updateElement: false,
      elementToUpdate: null,
    };
  }

  changeAddElementFlag = () => {
    if (this.state.addElement) {
      this.props.isInsideElementPage(true);
      this.props.getAllElements();
    } else {
      this.props.isInsideElementPage(false);
    }
    this.setState({ addElement: !this.state.addElement })
  }

  changeUpdateElementFlag = (element) => {
    if (this.state.updateElement) {
      this.props.getAllElements();
      this.props.isInsideElementPage(true);
      this.setState({ elementToUpdate: null })
    } else {
      this.props.isInsideElementPage(false);
      this.setState({ elementToUpdate: element })
    }
    this.setState({ updateElement: !this.state.updateElement })
  }

  render() {
    let addElementBtn;
    let component
    if (this.state.addElement) {
      addElementBtn = null;
      component = <AddElement user={this.props.user} changeAddElementFlag={this.changeAddElementFlag} />
    } else if (this.state.updateElement) {
      addElementBtn = null;
      component = <UpdateElement user={this.props.user} changeUpdateElementFlag={this.changeUpdateElementFlag} element={this.state.elementToUpdate} />
    }
    else {
      addElementBtn = <button onClick={() => this.changeAddElementFlag()}>Add Element</button>;
      component = this.props.elements.map(e => {
        if (e.type === 'Message Board') {
          return (
            <div style={{ display: 'inline-block' }} key={e.id} onClick={() => this.changeUpdateElementFlag(e)}>
              <MessageBoardElement key={e.id} element={e} />
            </div>
          )
        } else {
          return (
            <div style={{ display: 'inline-block' }} key={e.id} onClick={() => this.changeUpdateElementFlag(e)}>
              <RatingElement key={e.id} element={e} />
            </div>
          )
        }
      });
    }

    return (
      <div className="ManagerPlayground">
        <p>Welcome {this.props.user.username} || Logged in as manager</p>
        <br />
        {addElementBtn}
        <br />
        {component}
      </div>
    );
  }
}

export default ManagerPlayground;



