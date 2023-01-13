/* eslint-disable react/prop-types */
import styled from 'styled-components';
import { useState, useEffect } from 'react';

const SearchBox = () => {
  const [hasText, setHasText] = useState(false);
  const [inputValue, setInputValue] = useState('');
  const [options, setOptions] = useState(deselectedOptions);
  const [cursor, setCursor] = useState(0);
  //입력단어
  const onInputChange = (event) => {
    setInputValue(event.target.value);
    setHasText(true);
  };
  //x클릭시
  const onDeleteButtonClick = () => {
    setInputValue('');
  };
  //추천항목 클릭시
  const onDropDownClick = (clickedOption) => {
    setInputValue(clickedOption.target.textContent);
  };
  //단어가 들어가는 리스트 나열
  const onComboBox = (options) => {
    return options.map((option, idx) => (
      <li
        role="presentation"
        onClick={onDropDownClick}
        // className={cursor === idx ? 'selected' : 'unselected'}
        key={idx}
      >
        {option}{' '}
      </li>
    ));
  };

  useEffect(() => {
    if (inputValue === '') {
      setHasText(false);
      setOptions([]);
    } else {
      setHasText(true);
      setOptions(
        deselectedOptions.filter((option) => option.includes(inputValue))
      );
    }
  }, [inputValue]);

  return (
    <div className="autocomplete-wrapper">
      <SInputContainer>
        <input
          className="header-bottom-search"
          type="text"
          placeholder="브랜드명, 상품명으로 검색"
          value={inputValue}
          onChange={onInputChange}
        ></input>
        <div
          className="delete-button"
          role="presentation"
          onClick={onDeleteButtonClick}
        >
          &times;
        </div>
      </SInputContainer>
      {hasText ? <DropDown options={options} onComboBox={onComboBox} /> : null}
    </div>
  );
};
//선택단어에 맞는 리스트 보여줌
const DropDown = ({ options, onComboBox }) => {
  return <SAutoComplete>{onComboBox(options)}</SAutoComplete>;
};
const SInputContainer = styled.div`
  display: flex;
  align-items: center;
  margin-top: 10px;
  border: 2px solid black;
  position: releative;
  input {
    width: 30vw;
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
`;

const SAutoComplete = styled.div`
  position: absolute;
  width: 32vw;
  list-style-type: none;
  border-radius: 0 0 1rem 1rem;
  background-color: #ffffff;
  display: block;
  list-style-type: none;
  /* margin-top: -1px; */
  /* padding: 0.5rem 0; */
  border: 1px solid rgb(223, 225, 229);
  border-radius: 0 0 1rem 1rem;
  z-index: 3;
  > li {
    padding: 0.5rem 1rem;
    /* &.selected {
      background-color: lightgray;
    } */
  }
`;
const deselectedOptions = ['adidas', 'nike', 'newbalance', '데상트', 'ami'];

export default SearchBox;
