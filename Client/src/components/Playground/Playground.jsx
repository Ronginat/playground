import React, { Component } from 'react';
import './Playground.css';
import PlayerPlayground from './PlayerPlayground/PlayerPlayground';
import ManagerPlayground from './ManagerPlayground/ManagerPlayground';
import axios from 'axios';

const defaultPlayground = 'ratingplayground';
const size = 3;

class Playground extends Component {
  constructor(props) {
    super(props);
    this.state = {
      elements: [],
      page: 0,
      showPlaygroundBtns: true,
      getElementsFunctionName: 'getAllElements',
      attributeName: null,
      attributeValue: null
    };
    this.getAllElements();
  }

  getAllElements = (page) => {
    const newPage = page === undefined ? this.state.page : page;
    const loginUrl = `/elements/${defaultPlayground}/${this.props.user.email}/all`;
    const baseUrl = process.env.REACT_APP_API_URL;
    axios({
      url: loginUrl,
      baseURL: baseUrl,
      method: 'GET',
      params: { page: newPage, size: size }
    })
      .then(res => res.data)
      .then(res => {
        this.setState({
          elements: res,
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

  getElementsByAttribute = (page) => {
    console.log("getElementsByAttribute, ", this.state.attributeValue);
    if (this.state.attributeName === null || this.state.attributeValue === null)
      return;
    const newPage = page === undefined ? this.state.page : page;
    const loginUrl = `/elements/${defaultPlayground}/${this.props.user.email}/search/${this.state.attributeName}/${this.state.attributeValue}`;
    const baseUrl = process.env.REACT_APP_API_URL;
    axios({
      url: loginUrl,
      baseURL: baseUrl,
      method: 'GET',
      params: {
        page: newPage,
        size: size,
      }
    })
      .then(res => res.data)
      .then(res => {
        this.setState({
          elements: res,
          page: newPage
        });
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

  fetchElementsByChosenMethod = (page) => {
    if (this.state.getElementsFunctionName === "getAllElements") {
      this.getAllElements(page);
    } else { //search by attribute
      this.getElementsByAttribute(page);
    }
  }

  insideElementPage = (bool) => {
    this.setState({ showPlaygroundBtns: bool });
  }

  nextClick = () => {
    if (this.state.elements.length === size) {
      this.fetchElementsByChosenMethod(this.state.page + 1);
      //this.getAllElements(this.state.page + 1);
    } else {
      alert("last page");
    }
  };

  prevClick = () => {
    if (this.state.page > 0) {
      this.fetchElementsByChosenMethod(this.state.page - 1);
      //this.getAllElements(this.state.page - 1);
    } else {
      alert("first page");
    }
  };

  handleOptionChange = (changeEvent) => {
    if (changeEvent.target.value === "getElementsByName") {
      this.setState({
        getElementsFunctionName: "getElementsByName",
        attributeName: "name"
      });
    } else if (changeEvent.target.value === "getElementsByType") {
      this.setState({
        getElementsFunctionName: "getElementsByType",
        attributeName: "type"
      });
    } else {// if (changeEvent.target.value === "getAllElements") {
      this.setState({
        getElementsFunctionName: "getAllElements",
        attributeName: null
      });
      //this.getAllElements();
    }
  };

  render() {
    let component;
    if (this.props.user.role === 'manager') {
      component = <ManagerPlayground elements={this.state.elements} user={this.props.user} getAllElements={this.getAllElements} isInsideElementPage={this.insideElementPage}></ManagerPlayground>
    } else if (this.props.user.role === 'player') {
      component = <PlayerPlayground elements={this.state.elements} user={this.props.user} getAllElements={this.getAllElements} isInsideElementPage={this.insideElementPage}></PlayerPlayground>
    } else {
      component = <h1>Role not valid!!!</h1>
    }

    // if user is currently inside activity page, hide next,prev buttons
    let paginBtns, getFuncBtns;
    if (this.state.showPlaygroundBtns) {
      paginBtns = <div>
        <button onClick={() => this.prevClick()}>Previous</button>
        <button onClick={() => this.nextClick()}>Next</button>
      </div>

      getFuncBtns =
        <div className="fetchElement">
          <p>Get Elements:</p>
          <div className="fetchElementGetAll">
            <h6>Get All</h6>
            <input className="radio" type="radio" value="getAllElements"
              checked={this.state.getElementsFunctionName === "getAllElements"}
              onChange={this.handleOptionChange} />
          </div>
          <br />
          <div className="fetchElementAttributes">
            <h6>Search By Attribute:</h6>
            <h6>Name</h6>
            <input className="radio" type="radio" value="getElementsByName"
              checked={this.state.getElementsFunctionName === "getElementsByName"}
              onChange={this.handleOptionChange} />
            <h6>Type</h6>
            <input className="radio" type="radio" value="getElementsByType"
              checked={this.state.getElementsFunctionName === "getElementsByType"}
              onChange={this.handleOptionChange} />
            <div className="serchTextInputAndBtn">
              <input type="text" placeholder="Enter search value" name="search" onChange={event => this.setState({ attributeValue: event.target.value })} />
              <button onClick={() => this.fetchElementsByChosenMethod(0)}>Fetch</button>
            </div>
          </div>
        </div>
    }

    return (
      <div className="Playground">
        <button className="cancelBtn" onClick={() => this.props.logout()}>Logout</button>
        <button onClick={() => this.props.updateProfileFlag()}>Update User</button>
        {getFuncBtns}
        {component}
        {paginBtns}
      </div>
    );
  }
}

export default Playground;
