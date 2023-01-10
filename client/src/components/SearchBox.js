import styled from 'styled-components';
import { useState, useEffect } from 'react';

const SearchBox = () => {
  const [hasText, setHasText] = useState(false);
  const [inputValue, setInputValue] = useState('');
  const onInputChange = (event) => {
    setInputValue(event.target.value);
    setHasText(true);
  };
  return (
    <div className="autocomplete-wrapper">
      <SInputContainer>
        <input
          className="header-bottom-search"
          type="search"
          placeholder="브랜드명, 상품명으로 검색"
        ></input>
        <div className="delete-button">&times;</div>
      </SInputContainer>
      {/* <SAutoComplete /> */}
    </div>
  );

  // {
  //   /* {isClicked && <SAutocomplete />} */
  // })
};

const SInputContainer = styled.div`
  display: flex;
  align-items: center;
  margin-top: 10px;
  border: 2px solid black;
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
  }
`;

export default SearchBox;
