/* eslint-disable react/prop-types */
import styled from 'styled-components';
import { useState, useEffect } from 'react';
import { AiOutlineClose, AiOutlineSearch } from 'react-icons/ai';
import axios from 'axios';

const SearchBox = () => {
  const API_URL = process.env.REACT_APP_API_URL;
  const [hasText, setHasText] = useState(false);
  const [inputValue, setInputValue] = useState('');
  const [data, setData] = useState([]);
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
  useEffect(() => {
    if (!inputValue) {
      setHasText(false);
    }
  }, [inputValue]);
  useEffect(() => {
    window.scrollTo(0, 0);
    const fetchData = async () => {
      try {
        const response = await axios.get(API_URL + `boards`);
        setData(response.data.data);
        console.log(response.data.data);
      } catch {
        window.alert('오류가 발생했습니다.');
      }
    };
    fetchData();
  }, []);

  return (
    <div className="autocomplete-wrapper">
      <SInputContainer>
        <AiOutlineSearch />
        <input
          className="header-bottom-search"
          type="text"
          placeholder=" 상품명으로 검색"
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
            {data.products?.map((option, idx) => (
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
    width: 24vw;
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

const deselectedOptions = ['adidas', 'nike', 'newbalance', '데상트', 'ami'];

export default SearchBox;
