/* eslint-disable react/prop-types */
import styled from 'styled-components';
import { useState } from 'react';
import { AiOutlineClose, AiOutlineSearch } from 'react-icons/ai';

const SearchBox = () => {
  const [hasText, setHasText] = useState(false);
  const [inputValue, setInputValue] = useState('');
  // const [cursor, setCursor] = useState(0);
  // //입력단어
  const onInputChange = (event) => {
    setInputValue(event.target.value);
    setHasText(true);
  };
  //x클릭시
  const onDeleteButtonClick = () => {
    setInputValue('');
  };

  return (
    <div className="autocomplete-wrapper">
      <SInputContainer>
        <AiOutlineSearch />
        <input
          className="header-bottom-search"
          type="text"
          placeholder="브랜드명, 상품명으로 검색"
          list="autocomplete_options"
          value={inputValue}
          onChange={onInputChange}
          autoComplete="off"
        ></input>
        {hasText ? (
          <AiOutlineClose
            className="delete-button"
            role="presentation"
            onClick={onDeleteButtonClick}
          />
        ) : null}
        {hasText ? (
          <datalist id="autocomplete_options">
            {deselectedOptions.map((option, idx) => (
              <option key={idx} value={option}></option>
            ))}
          </datalist>
        ) : null}
      </SInputContainer>
    </div>
  );
};

const SInputContainer = styled.div`
  display: flex;
  align-items: center;
  margin-top: 20px;
  border: 2px solid #565656;
  border-top: 0;
  border-left: 0;
  border-right: 0;
  position: releative;
  input {
    width: 28vw;
    height: 5vh;
    border: none;
    background: transparent;
    flex-grow: 1;
    :focus {
      outline: none;
    }
  }
  .delete-button {
    margin-right: 10px;
    cursor: pointer;
  }
  /* datalist {
    position: absolute;
    width: 32vw;
    border-radius: 0 0 1rem 1rem;
    background-color: #ffffff;
    border: 1px solid rgb(223, 225, 229);
    border-radius: 0 0 1rem 1rem;
  }
  option {
    background-color: white;
    padding: 4px;
    color: black;
    margin-bottom: 1px;
    font-size: 18px;
    cursor: pointer;
  }
  option:hover,
  .active {
    background-color: lightblue;
  } */
`;

const deselectedOptions = ['adidas', 'nike', 'newbalance', '데상트', 'ami'];

export default SearchBox;
