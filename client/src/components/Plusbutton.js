import styled from 'styled-components';
import { AiOutlinePlus } from 'react-icons/ai';
// import { useState } from 'react';
import PropTypes from 'prop-types';

const PlusButton = ({ onClick }) => {
  return (
    <Swrapper>
      <button className="plus-btn">
        <AiOutlinePlus fill="#ffff" size="18" />
        <span className="text">추가</span>
      </button>
    </Swrapper>
  );
};

PlusButton.propTypes = {
  onClick: PropTypes.func,
};

const Swrapper = styled.div`
  display: flex;
  justify-content: center;
  width: 100%;

  .plus-btn {
    background-color: #d9d4a6;
    width: 4vw;
    height: 3vh;
    margin: 15px;
    color: #ffffff;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    .text {
      background-color: #d9d4a6;
      font-weight: 600;
    }
    svg {
      background-color: #d9d4a6;
    }
  }
`;
export default PlusButton;
